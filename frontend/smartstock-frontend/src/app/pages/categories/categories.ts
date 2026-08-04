import { CommonModule } from '@angular/common';
import {
  Component,
  OnInit,
  inject,
  ChangeDetectorRef
} from '@angular/core';
import { FormsModule } from '@angular/forms';
import {
  Router,
  RouterLink
} from '@angular/router';

import {
  Category,
  CategoryRequest
} from '../../core/models/category';
import { CategoryService } from '../../core/services/category';
import { AuthService } from '../../core/services/auth';

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


  loadCategories(): void {
    console.log('loadCategories called');

    this.isLoading = true;
    this.errorMessage = '';

    this.categoryService.getCategories().subscribe({
      next: categories => {
        console.log('Categories received:', categories);

        this.categories = categories;
        this.applySearch();

        this.isLoading = false;
        this.changeDetectorRef.detectChanges();
      },

      error: error => {
        console.error('Category API error:', error);

        this.isLoading = false;

        if (
          error.status === 401 ||
          error.status === 403
        ) {
          this.errorMessage =
            'Your session has expired. Please log in again.';
        } else {
          this.errorMessage =
            error.error?.message ??
            'Unable to load categories.';
        }

        this.changeDetectorRef.detectChanges();
      },

      complete: () => {
        console.log('Category request completed');
      }
    });
  }
  applySearch(): void {
    const search =
      this.searchTerm.trim().toLowerCase();

    if (!search) {
      this.filteredCategories = [
        ...this.categories
      ];
      return;
    }

    this.filteredCategories =
      this.categories.filter(category =>
        category.name
          .toLowerCase()
          .includes(search)
      );
  }

  openCreateModal(): void {
    this.selectedCategoryId = null;
    this.categoryName = '';
    this.errorMessage = '';
    this.showModal = true;
  }

  openEditModal(category: Category): void {
    this.selectedCategoryId = category.id;
    this.categoryName = category.name;
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
    const name = this.categoryName.trim();

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

    const categoryRequest =
      this.selectedCategoryId === null
        ? this.categoryService
          .createCategory(request)
        : this.categoryService
          .updateCategory(
            this.selectedCategoryId,
            request
          );

    categoryRequest.subscribe({
      next: () => {
        this.successMessage =
          this.selectedCategoryId === null
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
        console.error(error);
        this.errorMessage =
          error.error?.message ??
          'Unable to save category.';
        this.isSaving = false;
      }
    });
  }

  deleteCategory(category: Category): void {
    const confirmed = window.confirm(
      `Delete category "${category.name}"?`
    );

    if (!confirmed) {
      return;
    }

    this.errorMessage = '';

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
          console.error(error);
          this.errorMessage =
            error.error?.message ??
            'Unable to delete category. It may be used by a product.';
        }
      });
  }

  logout(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}