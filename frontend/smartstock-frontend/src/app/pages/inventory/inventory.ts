import { CommonModule } from '@angular/common';
import {
  ChangeDetectorRef,
  Component,
  OnInit,
  inject
} from '@angular/core';

import {
  FormBuilder,
  ReactiveFormsModule,
  Validators
} from '@angular/forms';

import { finalize } from 'rxjs';

import {
  Inventory
} from '../../core/models/inventory';

import {
  InventoryService
} from '../../core/services/inventory';

@Component({
  selector: 'app-inventory',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule
  ],
  templateUrl: './inventory.html',
  styleUrl: './inventory.css'
})
export class InventoryPage implements OnInit {
  private readonly inventoryService =
    inject(InventoryService);

  private readonly formBuilder =
    inject(FormBuilder);

  private readonly changeDetector =
    inject(ChangeDetectorRef);

  inventoryItems: Inventory[] = [];
  filteredInventory: Inventory[] = [];

  isLoading = true;
  isSaving = false;

  errorMessage = '';
  successMessage = '';

  searchValue = '';
  selectedStatus = 'ALL';

  showStockModal = false;
  selectedInventory: Inventory | null = null;
  stockAction: 'IN' | 'OUT' = 'IN';

  stockForm = this.formBuilder.nonNullable.group({
    quantity: [
      1,
      [
        Validators.required,
        Validators.min(1)
      ]
    ]
  });

  ngOnInit(): void {
    this.loadInventory();
  }

  loadInventory(): void {
    this.isLoading = true;
    this.errorMessage = '';

    this.inventoryService
      .getInventory()
      .pipe(
        finalize(() => {
          this.isLoading = false;
          this.changeDetector.detectChanges();
        })
      )
      .subscribe({
        next: inventory => {
          this.inventoryItems = inventory;
          this.applyFilters();
        },

        error: error => {
          console.error(
            'Failed to load inventory:',
            error
          );

          this.errorMessage =
            error.error?.message ??
            'Unable to load inventory.';
        }
      });
  }

  search(event: Event): void {
    const input =
      event.target as HTMLInputElement;

    this.searchValue = input.value;
    this.applyFilters();
  }

  setStatusFilter(status: string): void {
    this.selectedStatus = status;
    this.applyFilters();
  }

  applyFilters(): void {
    const search =
      this.searchValue.trim().toLowerCase();

    this.filteredInventory =
      this.inventoryItems.filter(item => {
        const matchesSearch =
          !search ||
          item.productName
            .toLowerCase()
            .includes(search);

        const status =
          this.getStatus(item);

        const matchesStatus =
          this.selectedStatus === 'ALL' ||
          this.selectedStatus === status;

        return matchesSearch && matchesStatus;
      });
  }

  getStatus(
    item: Inventory
  ): 'HEALTHY' | 'LOW' | 'OUT' {
    if (item.stockLevel === 0) {
      return 'OUT';
    }

    if (
      item.stockLevel <= item.minimumStock
    ) {
      return 'LOW';
    }

    return 'HEALTHY';
  }

  getStatusLabel(item: Inventory): string {
    const status = this.getStatus(item);

    if (status === 'OUT') {
      return 'Out of stock';
    }

    if (status === 'LOW') {
      return 'Low stock';
    }

    return 'Healthy';
  }

  openStockModal(
    item: Inventory,
    action: 'IN' | 'OUT'
  ): void {
    this.selectedInventory = item;
    this.stockAction = action;

    this.stockForm.reset({
      quantity: 1
    });

    this.errorMessage = '';
    this.showStockModal = true;
  }

  closeStockModal(): void {
    if (this.isSaving) {
      return;
    }

    this.showStockModal = false;
    this.selectedInventory = null;
  }

  submitStockChange(): void {
    if (
      this.stockForm.invalid ||
      !this.selectedInventory
    ) {
      this.stockForm.markAllAsTouched();
      return;
    }

    const quantity =
      Number(
        this.stockForm.controls
          .quantity.value
      );

    if (
      this.stockAction === 'OUT' &&
      quantity >
        this.selectedInventory.stockLevel
    ) {
      this.errorMessage =
        'Stock-out quantity cannot exceed the current stock level.';
      return;
    }

    this.isSaving = true;
    this.errorMessage = '';
    this.successMessage = '';

    const operation =
      this.stockAction === 'IN'
        ? this.inventoryService.stockIn(
            this.selectedInventory.id,
            quantity
          )
        : this.inventoryService.stockOut(
            this.selectedInventory.id,
            quantity
          );

    operation
      .pipe(
        finalize(() => {
          this.isSaving = false;
          this.changeDetector.detectChanges();
        })
      )
      .subscribe({
        next: response => {
          this.successMessage =
            response.message;

          this.closeStockModal();
          this.loadInventory();
        },

        error: error => {
          console.error(
            'Stock operation failed:',
            error
          );

          this.errorMessage =
            error.error?.message ??
            'Unable to update stock.';
        }
      });
  }

  deleteInventory(item: Inventory): void {
    const confirmed = window.confirm(
      `Delete inventory for "${item.productName}"?`
    );

    if (!confirmed) {
      return;
    }

    this.inventoryService
      .deleteInventory(item.id)
      .subscribe({
        next: () => {
          this.successMessage =
            'Inventory deleted successfully.';

          this.loadInventory();
        },

        error: error => {
          console.error(
            'Delete inventory failed:',
            error
          );

          this.errorMessage =
            error.error?.message ??
            'Unable to delete inventory.';
        }
      });
  }

  get totalItems(): number {
    return this.inventoryItems.length;
  }

  get lowStockCount(): number {
    return this.inventoryItems.filter(
      item => this.getStatus(item) === 'LOW'
    ).length;
  }

  get outOfStockCount(): number {
    return this.inventoryItems.filter(
      item => this.getStatus(item) === 'OUT'
    ).length;
  }

  get healthyCount(): number {
    return this.inventoryItems.filter(
      item =>
        this.getStatus(item) === 'HEALTHY'
    ).length;
  }
}
