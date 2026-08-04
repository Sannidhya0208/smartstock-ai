import { CommonModule } from '@angular/common';
import {
  ChangeDetectorRef,
  Component,
  OnInit,
  inject
} from '@angular/core';
import { FormsModule } from '@angular/forms';

import {
  Supplier,
  SupplierRequest
} from '../../core/models/supplier';
import { SupplierService } from '../../core/services/supplier';

@Component({
  selector: 'app-suppliers',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule
  ],
  templateUrl: './supplier.html',
  styleUrl: './supplier.css'
})
export class Suppliers implements OnInit {

  private readonly supplierService =
    inject(SupplierService);

  private readonly cdr =
    inject(ChangeDetectorRef);

  suppliers: Supplier[] = [];
  filteredSuppliers: Supplier[] = [];

  searchTerm = '';

  supplierName = '';
  supplierEmail = '';
  supplierPhone = '';

  selectedSupplierId: number | null = null;

  isLoading = false;
  isSaving = false;
  showModal = false;

  errorMessage = '';
  successMessage = '';

  ngOnInit(): void {
    this.loadSuppliers();
  }

  loadSuppliers(): void {
    this.isLoading = true;
    this.errorMessage = '';

    this.supplierService.getSuppliers().subscribe({
      next: suppliers => {
        this.suppliers = suppliers;
        this.applySearch();
        this.isLoading = false;
        this.cdr.detectChanges();
      },
      error: error => {
        console.error('Supplier API error:', error);

        this.isLoading = false;
        this.errorMessage =
          error.error?.message ??
          'Unable to load suppliers.';

        this.cdr.detectChanges();
      }
    });
  }

  applySearch(): void {
    const search =
      this.searchTerm.trim().toLowerCase();

    if (!search) {
      this.filteredSuppliers = [
        ...this.suppliers
      ];
      return;
    }

    this.filteredSuppliers =
      this.suppliers.filter(supplier =>
        supplier.name
          .toLowerCase()
          .includes(search) ||
        supplier.email
          .toLowerCase()
          .includes(search) ||
        supplier.phone
          .toLowerCase()
          .includes(search)
      );
  }

  openCreateModal(): void {
    this.selectedSupplierId = null;
    this.supplierName = '';
    this.supplierEmail = '';
    this.supplierPhone = '';
    this.errorMessage = '';
    this.showModal = true;
  }

  openEditModal(supplier: Supplier): void {
    this.selectedSupplierId = supplier.id;
    this.supplierName = supplier.name;
    this.supplierEmail = supplier.email;
    this.supplierPhone = supplier.phone;
    this.errorMessage = '';
    this.showModal = true;
  }

  closeModal(): void {
    if (this.isSaving) {
      return;
    }

    this.showModal = false;
    this.selectedSupplierId = null;
    this.supplierName = '';
    this.supplierEmail = '';
    this.supplierPhone = '';
    this.errorMessage = '';
  }

  saveSupplier(): void {
    const name = this.supplierName.trim();
    const email = this.supplierEmail.trim();
    const phone = this.supplierPhone.trim();

    if (!name || !email || !phone) {
      this.errorMessage =
        'Name, email and phone are required.';
      return;
    }

    const request: SupplierRequest = {
      name,
      email,
      phone
    };

    this.isSaving = true;
    this.errorMessage = '';

    const supplierRequest =
      this.selectedSupplierId === null
        ? this.supplierService
            .createSupplier(request)
        : this.supplierService
            .updateSupplier(
              this.selectedSupplierId,
              request
            );

    supplierRequest.subscribe({
      next: () => {
        this.successMessage =
          this.selectedSupplierId === null
            ? 'Supplier created successfully.'
            : 'Supplier updated successfully.';

        this.isSaving = false;
        this.closeModal();
        this.loadSuppliers();

        setTimeout(() => {
          this.successMessage = '';
          this.cdr.detectChanges();
        }, 3000);
      },
      error: error => {
        console.error('Save supplier error:', error);

        this.isSaving = false;
        this.errorMessage =
          error.error?.message ??
          'Unable to save supplier.';

        this.cdr.detectChanges();
      }
    });
  }

  deleteSupplier(supplier: Supplier): void {
    const confirmed = window.confirm(
      `Delete supplier "${supplier.name}"?`
    );

    if (!confirmed) {
      return;
    }

    this.errorMessage = '';

    this.supplierService
      .deleteSupplier(supplier.id)
      .subscribe({
        next: () => {
          this.successMessage =
            'Supplier deleted successfully.';

          this.loadSuppliers();

          setTimeout(() => {
            this.successMessage = '';
            this.cdr.detectChanges();
          }, 3000);
        },
        error: error => {
          console.error(
            'Delete supplier error:',
            error
          );

          this.errorMessage =
            error.error?.message ??
            'Unable to delete supplier. It may be linked to a product.';

          this.cdr.detectChanges();
        }
      });
  }
}