import {
  ChangeDetectorRef,
  Component,
  OnInit
} from '@angular/core';

import {
  CommonModule
} from '@angular/common';

import {
  FormBuilder,
  FormGroup,
  ReactiveFormsModule,
  Validators
} from '@angular/forms';

import {
  finalize
} from 'rxjs';

import {
  UserManagementService,
  UserResponse
} from '../../core/services/user-management.service';

@Component({
  selector: 'app-users',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule
  ],
  templateUrl: './users.html',
  styleUrl: './users.css'
})
export class Users implements OnInit {

  users: UserResponse[] = [];

  isLoading = false;
  isSaving = false;

  showCreateForm = false;

  successMessage = '';
  errorMessage = '';

  userForm: FormGroup;

  constructor(
    private readonly userManagementService:
      UserManagementService,

    private readonly formBuilder:
      FormBuilder,

    private readonly changeDetector:
      ChangeDetectorRef
  ) {

    this.userForm =
      this.formBuilder.group({
        name: [
          '',
          [
            Validators.required,
            Validators.minLength(2)
          ]
        ],

        email: [
          '',
          [
            Validators.required,
            Validators.email
          ]
        ],

        password: [
          '',
          [
            Validators.required,
            Validators.minLength(8)
          ]
        ],

        role: [
          'STAFF',
          Validators.required
        ]
      });
  }

  ngOnInit(): void {
    this.loadUsers();
  }

  loadUsers(): void {

    this.isLoading = true;
    this.errorMessage = '';

    this.userManagementService
      .getUsers()
      .pipe(
        finalize(() => {
          this.isLoading = false;
          this.changeDetector.detectChanges();
        })
      )
      .subscribe({
        next: users => {
          this.users = users;
        },

        error: error => {
          console.error(
            'Failed to load users:',
            error
          );

          this.errorMessage =
            error.error?.message ??
            'Unable to load users.';
        }
      });
  }

  openCreateForm(): void {

    this.userForm.reset({
      name: '',
      email: '',
      password: '',
      role: 'STAFF'
    });

    this.errorMessage = '';
    this.successMessage = '';
    this.showCreateForm = true;
  }

  closeCreateForm(): void {

    if (this.isSaving) {
      return;
    }

    this.showCreateForm = false;
  }

  createUser(): void {

    if (this.userForm.invalid) {
      this.userForm.markAllAsTouched();
      return;
    }

    const request =
      this.userForm.getRawValue();

    this.isSaving = true;

    this.errorMessage = '';
    this.successMessage = '';

    this.userManagementService
      .createUser(request)
      .pipe(
        finalize(() => {
          this.isSaving = false;
          this.changeDetector.detectChanges();
        })
      )
      .subscribe({
        next: () => {

          this.successMessage =
            'User created successfully.';

          this.closeCreateForm();

          this.loadUsers();
        },

        error: error => {

          console.error(
            'Failed to create user:',
            error
          );

          this.errorMessage =
            error.error?.message ??
            'Unable to create user.';
        }
      });
  }

  changeRole(
    user: UserResponse,
    event: Event
  ): void {

    const select =
      event.target as HTMLSelectElement;

    const role =
      select.value as 'MANAGER' | 'STAFF';

    if (role === user.role) {
      return;
    }

    this.errorMessage = '';
    this.successMessage = '';

    this.userManagementService
      .updateRole(
        user.id,
        role
      )
      .subscribe({
        next: updatedUser => {

          user.role =
            updatedUser.role;

          this.successMessage =
            `${user.name}'s role was updated successfully.`;

          this.changeDetector.detectChanges();
        },

        error: error => {

          console.error(
            'Failed to update role:',
            error
          );

          this.errorMessage =
            error.error?.message ??
            'Unable to update user role.';

          this.loadUsers();
        }
      });
  }

  toggleStatus(
    user: UserResponse
  ): void {

    const newStatus =
      !user.active;

    this.errorMessage = '';
    this.successMessage = '';

    this.userManagementService
      .updateStatus(
        user.id,
        newStatus
      )
      .subscribe({
        next: updatedUser => {

          user.active =
            updatedUser.active;

          this.successMessage =
            updatedUser.active
              ? `${user.name} has been enabled.`
              : `${user.name} has been disabled.`;

          this.changeDetector.detectChanges();
        },

        error: error => {

          console.error(
            'Failed to update user status:',
            error
          );

          this.errorMessage =
            error.error?.message ??
            'Unable to update user status.';
        }
      });
  }

  deleteUser(
    user: UserResponse
  ): void {

    const confirmed =
      window.confirm(
        `Delete ${user.name}? This action cannot be undone.`
      );

    if (!confirmed) {
      return;
    }

    this.errorMessage = '';
    this.successMessage = '';

    this.userManagementService
      .deleteUser(user.id)
      .subscribe({
        next: () => {

          this.successMessage =
            `${user.name} was deleted successfully.`;

          this.users =
            this.users.filter(
              existingUser =>
                existingUser.id !== user.id
            );

          this.changeDetector.detectChanges();
        },

        error: error => {

          console.error(
            'Failed to delete user:',
            error
          );

          this.errorMessage =
            error.error?.message ??
            'Unable to delete user.';
        }
      });
  }

  formatLastLogin(
    value: string | null
  ): string {

    if (!value) {
      return 'Never';
    }

    return new Date(value)
      .toLocaleString();
  }
}
