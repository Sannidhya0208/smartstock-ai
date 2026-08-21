import { CommonModule } from '@angular/common';
import {
  Component,
  OnInit,
  inject,
  ChangeDetectorRef
} from '@angular/core';

import { FormsModule } from '@angular/forms';

import {
  Router
} from '@angular/router';

import {
  Category,
  CategoryRequest
} from '../../core/models/category';

import {
  CategoryService
} from '../../core/services/category';

import {
  AuthService
} from '../../core/services/auth';

@Component({
  selector: 'app-categories',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule
  ],
  templateUrl: './categories.html',
  styleUrl: './categories.css'
})
export class Categories implements OnInit {

  private readonly categoryService =
    inject(CategoryService);

  private readonly authService =
    inject(AuthService);

  private readonly router =
    inject(Router);

  private readonly changeDetectorRef =
    inject(ChangeDetectorRef);

  categories: Category[] = [];

  filteredCategories: Category[] = [];

  searchTerm = '';

  categoryName = '';

  selectedCategoryId: number | null = null;

  isLoading = false;

  isSaving = false;

  showModal = false;

  errorMessage = '';

  successMessage = '';

  ngOnInit(): void {
    this.loadCategories();
  }

  canCreateCategory(): boolean {
    return this.authService.isOwner()
      || this.authService.isManager();
  }

  canEditCategory(): boolean {
    return this.authService.isOwner()
      || this.authService.isManager();
  }

  canDeleteCategory(): boolean {
    return this.authService.isOwner();
  }

  canManageCategories(): boolean {
    return this.canEditCategory()
      || this.canDeleteCategory();
  }

  loadCategories(): void {

    this.isLoading = true;

    this.errorMessage = '';

    this.categoryService
      .getCategories()
      .subscribe({

        next: categories => {

          this.categories = categories;

          this.applySearch();

          this.isLoading = false;

          this.changeDetectorRef
            .detectChanges();
        },

        error: error => {

          console.error(
            'Category API error:',
            error
          );

          this.isLoading = false;

          if (
            error.status === 401
            || error.status === 403
          ) {

            this.errorMessage =
              'Your session has expired or you do not have permission.';

          } else {

            this.errorMessage =
              error.error?.message
              ?? 'Unable to load categories.';
          }

          this.changeDetectorRef
            .detectChanges();
        }
      });
  }

  applySearch(): void {

    const search =
      this.searchTerm
        .trim()
        .toLowerCase();

    if (!search) {

      this.filteredCategories = [
        ...this.categories
      ];

      return;
    }

    this.filteredCategories =
      this.categories.filter(
        category =>
          category.name
            .toLowerCase()
            .includes(search)
      );
  }

  openCreateModal(): void {

    if (!this.canCreateCategory()) {
      return;
    }

    this.selectedCategoryId = null;

    this.categoryName = '';

    this.errorMessage = '';

    this.showModal = true;
  }

  openEditModal(
    category: Category
  ): void {

    if (!this.canEditCategory()) {
      return;
    }

    this.selectedCategoryId =
      category.id;

    this.categoryName =
      category.name;

    this.errorMessage = '';

    this.showModal = true;
  }

  closeModal(): void {

    if (this.isSaving) {
      return;
    }

    this.showModal = false;

    this.selectedCategoryId = null;

    this.categoryName = '';
  }

  saveCategory(): void {

    const isCreating =
      this.selectedCategoryId === null;

    if (
      isCreating
      && !this.canCreateCategory()
    ) {
      return;
    }

    if (
      !isCreating
      && !this.canEditCategory()
    ) {
      return;
    }

    const name =
      this.categoryName.trim();

    if (!name) {

      this.errorMessage =
        'Category name is required.';

      return;
    }

    const request: CategoryRequest = {
      name
    };

    this.isSaving = true;

    this.errorMessage = '';

    this.successMessage = '';

    const categoryRequest =
      isCreating

        ? this.categoryService
            .createCategory(request)

        : this.categoryService
            .updateCategory(
              this.selectedCategoryId!,
              request
            );

    categoryRequest.subscribe({

      next: () => {

        this.successMessage =
          isCreating
            ? 'Category created successfully.'
            : 'Category updated successfully.';

        this.isSaving = false;

        this.closeModal();

        this.loadCategories();

        setTimeout(() => {
          this.successMessage = '';
        }, 3000);
      },

      error: error => {

        console.error(
          'Failed to save category:',
          error
        );

        this.errorMessage =
          error.error?.message
          ?? 'Unable to save category.';

        this.isSaving = false;

        this.changeDetectorRef
          .detectChanges();
      }
    });
  }

  deleteCategory(
    category: Category
  ): void {

    if (!this.canDeleteCategory()) {
      return;
    }

    const confirmed =
      window.confirm(
        `Delete category "${category.name}"?`
      );

    if (!confirmed) {
      return;
    }

    this.errorMessage = '';

    this.successMessage = '';

    this.categoryService
      .deleteCategory(category.id)
      .subscribe({

        next: () => {

          this.successMessage =
            'Category deleted successfully.';

          this.loadCategories();

          setTimeout(() => {
            this.successMessage = '';
          }, 3000);
        },

        error: error => {

          console.error(
            'Failed to delete category:',
            error
          );

          this.errorMessage =
            error.error?.message
            ?? 'Unable to delete category. It may be used by a product.';

          this.changeDetectorRef
            .detectChanges();
        }
      });
  }

  logout(): void {

    this.authService.logout();

    this.router.navigate([
      '/login'
    ]);
  }
}