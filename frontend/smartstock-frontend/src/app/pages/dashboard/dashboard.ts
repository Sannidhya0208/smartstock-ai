import {
  ChangeDetectorRef,
  Component,
  inject,
  OnInit
} from '@angular/core';

import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { finalize } from 'rxjs';

import {
  AuthService
} from '../../core/services/auth';

import {
  DashboardService
} from '../../core/services/dashboard';

import {
  DashboardResponse
} from '../../core/models/dashboard-response';

import {
  AiDashboardResponse
} from '../../core/models/ai-dashboard-response';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [
    CommonModule
  ],
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.css'
})
export class Dashboard implements OnInit {

  private readonly authService =
    inject(AuthService);

  private readonly dashboardService =
    inject(DashboardService);

  private readonly router =
    inject(Router);

  private readonly changeDetector =
    inject(ChangeDetectorRef);

  dashboard: DashboardResponse | null = null;

  aiDashboard: AiDashboardResponse | null = null;

  isLoading = true;

  isAiLoading = false;

  errorMessage = '';

  aiErrorMessage = '';

  ngOnInit(): void {
    this.loadDashboard();
  }

  canUseAi(): boolean {
    return this.authService.isOwner()
      || this.authService.isManager();
  }

  loadDashboard(): void {

    this.isLoading = true;
    this.errorMessage = '';
    this.dashboard = null;

    this.dashboardService
      .getDashboard()
      .pipe(
        finalize(() => {
          this.isLoading = false;
          this.changeDetector.detectChanges();
        })
      )
      .subscribe({

        next: response => {

          this.dashboard = response;

          // OWNER and MANAGER get AI summary
          if (this.canUseAi()) {
            this.loadAiDashboardSummary();
          }
        },

        error: error => {

          console.error(
            'Dashboard request failed:',
            error
          );

          if (error.status === 401) {
            this.authService.logout();
            this.router.navigate(['/login']);
            return;
          }

          if (error.status === 403) {
            this.errorMessage =
              'You do not have permission to access the dashboard.';
            return;
          }

          if (error.status === 0) {
            this.errorMessage =
              'Unable to connect to the backend server.';
            return;
          }

          this.errorMessage =
            error.error?.message ??
            'Unable to load dashboard information.';
        }
      });
  }
  loadAiDashboardSummary(): void {

    this.isAiLoading = true;
    this.aiErrorMessage = '';
    this.aiDashboard = null;

    this.dashboardService
      .getAiDashboardSummary()
      .pipe(
        finalize(() => {
          this.isAiLoading = false;
          this.changeDetector.detectChanges();
        })
      )
      .subscribe({

        next: response => {

          console.log(
            'AI dashboard response:',
            response
          );

          this.aiDashboard = response;
        },

        error: error => {

          console.error(
            'AI dashboard request failed:',
            error
          );

          if (error.status === 503) {
            this.aiErrorMessage =
              'AI service is currently unavailable.';
            return;
          }

          if (error.status === 403) {
            this.aiErrorMessage =
              'You do not have permission to use AI features.';
            return;
          }

          this.aiErrorMessage =
            error.error?.message ??
            'Unable to generate AI inventory summary.';
        }
      });
  }
  logout(): void {

    this.authService.logout();

    this.router.navigate([
      '/login'
    ]);
  }

  openProducts(): void {

    this.router.navigate([
      '/products'
    ]);
  }
}