import {
  ChangeDetectorRef,
  Component,
  inject,
  OnInit
} from '@angular/core';

import { CommonModule } from '@angular/common';
import {
  FormBuilder,
  ReactiveFormsModule,
  Validators
} from '@angular/forms';

import { finalize, forkJoin } from 'rxjs';

import { Product } from '../../core/models/product';
import {
  ProductRequest
} from '../../core/models/product-request';
import {
  ProductPageResponse
} from '../../core/models/product-page-response';
import { Category } from '../../core/models/category';
import { Supplier } from '../../core/models/supplier';

import {
  ProductService
} from '../../core/services/product';
import {
  CategoryService
} from '../../core/services/category';
import {
  SupplierService
} from '../../core/services/supplier';

@Component({
  selector: 'app-products',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule
  ],
  templateUrl: './products.html',
  styleUrl: './products.css'
})
export class Products implements OnInit {
  private readonly productService =
    inject(ProductService);

  private readonly categoryService =
    inject(CategoryService);

  private readonly supplierService =
    inject(SupplierService);

  private readonly formBuilder =
    inject(FormBuilder);

  private readonly changeDetector =
    inject(ChangeDetectorRef);

  products: Product[] = [];
  categories: Category[] = [];
  suppliers: Supplier[] = [];

  isLoading = true;
  isSaving = false;
  errorMessage = '';
  successMessage = '';
  searchTerm: string = '';

  searchValue = '';

  currentPage = 0;
  pageSize = 10;
  totalElements = 0;
  totalPages = 0;
  isLastPage = true;

  sortBy = 'id';
  sortDirection = 'asc';

  showForm = false;
  editingProductId: number | null = null;

  productForm = this.formBuilder.nonNullable.group({
    name: [
      '',
      [
        Validators.required,
        Validators.minLength(2)
      ]
    ],
    sku: [
      '',
      [
        Validators.required,
        Validators.minLength(2)
      ]
    ],
    price: [
      0,
      [
        Validators.required,
        Validators.min(0)
      ]
    ],
    quantity: [
      0,
      [
        Validators.required,
        Validators.min(0)
      ]
    ],
    categoryId: [
      0,
      [
        Validators.required,
        Validators.min(1)
      ]
    ],
    supplierId: [
      0,
      [
        Validators.required,
        Validators.min(1)
      ]
    ],
    addToInventory: [
      false,
      [
        Validators.required
      ]
    ],
    stockLevel: [
      0,
      [
        Validators.required,
        Validators.min(0)
      ]
    ],
    minimumStock: [
      0,
      [
        Validators.required,
        Validators.min(0)
      ]
    ]
  });

  ngOnInit(): void {
    this.loadInitialData();
  }

  loadInitialData(): void {
    this.isLoading = true;
    this.errorMessage = '';

    forkJoin({
      categories:
        this.categoryService.getCategories(),
      suppliers:
        this.supplierService.getSuppliers()
    }).subscribe({
      next: result => {
        this.categories = result.categories;
        this.suppliers = result.suppliers;
        this.loadProducts();
      },
      error: error => {
        console.error(
          'Failed to load form options:',
          error
        );

        this.isLoading = false;
        this.errorMessage =
          'Unable to load categories or suppliers.';

        this.changeDetector.detectChanges();
      }
    });
  }

  loadProducts(): void {
    this.isLoading = true;
    this.errorMessage = '';

    this.productService.getProducts(
      this.currentPage,
      this.pageSize,
      this.sortBy,
      this.sortDirection,
      this.searchTerm
    ).subscribe({
      next: (response: ProductPageResponse) => {
        console.log('Products response:', response);

        this.products = response.content ?? [];
        this.currentPage = response.pageNumber;
        this.pageSize = response.pageSize;
        this.totalElements = response.totalElements;
        this.totalPages = response.totalPages;

        this.isLoading = false;
        this.changeDetector.detectChanges();
      },
      error: (error) => {
        console.error('Failed to load products:', error);

        this.products = [];
        this.errorMessage = 'Unable to load products.';
        this.isLoading = false;
        this.changeDetector.detectChanges();
      }
    });
  }

  search(event: Event): void {
    const input = event.target as HTMLInputElement;

    this.searchValue = input.value;
    this.currentPage = 0;
    this.loadProducts();
  }

  clearSearch(): void {
    this.searchValue = '';
    this.currentPage = 0;
    this.loadProducts();
  }

  openCreateForm(): void {
    this.editingProductId = null;
    this.successMessage = '';
    this.errorMessage = '';

    this.productForm.reset({
      name: '',
      sku: '',
      price: 0,
      quantity: 0,
      categoryId: 0,
      supplierId: 0,
      addToInventory: false,
      stockLevel: 0,
      minimumStock: 0
    });

    this.showForm = true;
  }

  openEditForm(product: Product): void {
    this.editingProductId = product.id;
    this.errorMessage = '';
    this.successMessage = '';

    this.productForm.patchValue({
      name: product.name,
      sku: product.sku,
      price: product.price,
      quantity: product.quantity,
      categoryId: product.categoryId ?? 0,
      supplierId: product.supplierId ?? 0,

      // Inventory is not recreated while editing.
      addToInventory: false,
      minimumStock: 10
    });

    this.showForm = true;
  }
  closeForm(): void {
    this.showForm = false;
    this.editingProductId = null;
    this.productForm.reset();
  }

  saveProduct(): void {
    if (this.productForm.invalid) {
      this.productForm.markAllAsTouched();
      return;
    }

    const formValue =
      this.productForm.getRawValue();

    const isCreating =
      this.editingProductId === null;

    const quantity =
      Number(formValue.quantity ?? 0);

    const addToInventory =
        formValue.addToInventory ?? false;

    const request: ProductRequest = {
      name: formValue.name?.trim() ?? '',
      sku: formValue.sku?.trim() ?? '',
      price: Number(formValue.price ?? 0),
      quantity,
      categoryId: Number(
        formValue.categoryId ?? 0
      ),
      supplierId: Number(
        formValue.supplierId ?? 0
      ),

      addToInventory,

      stockLevel: addToInventory
        ? quantity
        : 0,

      minimumStock: addToInventory
        ? Number(
          formValue.minimumStock ?? 10
        )
        : 0
    };

    if (
      request.categoryId <= 0 ||
      request.supplierId <= 0
    ) {
      this.errorMessage =
        'Please select a category and supplier.';
      return;
    }

    if (
      request.price < 0 ||
      request.quantity < 0
    ) {
      this.errorMessage =
        'Price and quantity cannot be negative.';
      return;
    }


    this.isSaving = true;
    this.errorMessage = '';
    this.successMessage = '';

    const operation =
      isCreating
        ? this.productService.createProduct(
          request
        )
        : this.productService.updateProduct(
          this.editingProductId!,
          request
        );

    operation
      .pipe(
        finalize(() => {
          this.isSaving = false;
          this.changeDetector.detectChanges();
        })
      )
      .subscribe({
        next: () => {
          this.successMessage =
            isCreating
              ? 'Product created successfully.'
              : 'Product updated successfully.';

          this.closeForm();
          this.loadProducts();
        },

        error: error => {
          console.error(
            'Failed to save product:',
            error
          );

          this.errorMessage =
            error.error?.message ??
            'Unable to save the product.';

          this.changeDetector.detectChanges();
        }
      });
  }

  deleteProduct(product: Product): void {
    const confirmed = window.confirm(
      `Delete "${product.name}"? ` +
      'This action cannot be undone.'
    );

    if (!confirmed) {
      return;
    }

    this.errorMessage = '';
    this.successMessage = '';

    this.productService
      .deleteProduct(product.id)
      .subscribe({
        next: () => {
          this.successMessage =
            'Product deleted successfully.';

          if (
            this.products.length === 1 &&
            this.currentPage > 0
          ) {
            this.currentPage--;
          }

          this.loadProducts();
        },
        error: error => {
          console.error(
            'Failed to delete product:',
            error
          );

          this.errorMessage =
            error.error?.message ??
            'Unable to delete the product.';
        }
      });
  }

  sort(column: string): void {
    if (this.sortBy === column) {
      this.sortDirection =
        this.sortDirection === 'asc'
          ? 'desc'
          : 'asc';
    } else {
      this.sortBy = column;
      this.sortDirection = 'asc';
    }

    this.currentPage = 0;
    this.loadProducts();
  }

  previousPage(): void {
    if (this.currentPage === 0) {
      return;
    }

    this.currentPage--;
    this.loadProducts();
  }

  nextPage(): void {
    if (this.isLastPage) {
      return;
    }

    this.currentPage++;
    this.loadProducts();
  }

  get startRecord(): number {
    if (this.totalElements === 0) {
      return 0;
    }

    return this.currentPage * this.pageSize + 1;
  }

  get endRecord(): number {
    return Math.min(
      (this.currentPage + 1) * this.pageSize,
      this.totalElements
    );
  }

  get isEditing(): boolean {
    return this.editingProductId !== null;
  }
}