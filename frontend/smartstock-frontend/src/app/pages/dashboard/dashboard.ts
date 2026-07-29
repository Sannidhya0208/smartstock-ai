import {
  ChangeDetectorRef,
  Component,
  inject,
  OnInit
} from '@angular/core';

import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { finalize } from 'rxjs';

import { AuthService } from '../../core/services/auth';
import {
  DashboardService
} from '../../core/services/dashboard';

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
  private readonly authService = inject(AuthService);
  private readonly dashboardService =
    inject(DashboardService);
  private readonly router = inject(Router);
  private readonly changeDetector =
    inject(ChangeDetectorRef);

  dashboard: AiDashboardResponse | null = null;
  isLoading = true;
  errorMessage = '';

  ngOnInit(): void {
    this.loadDashboard();
  }

  loadDashboard(): void {
    this.isLoading = true;
    this.errorMessage = '';
    this.dashboard = null;

    this.dashboardService
      .getDashboardSummary()
      .pipe(
        finalize(() => {
          this.isLoading = false;
          this.changeDetector.detectChanges();
        })
      )
      .subscribe({
        next: response => {
          console.log(
            'Dashboard response:',
            response
          );

          this.dashboard = response;
        },

        error: error => {
          console.error(
            'Dashboard request failed:',
            error
          );

          if (
            error.status === 401 ||
            error.status === 403
          ) {
            this.authService.logout();
            this.router.navigate(['/login']);
            return;
          }

          if (error.status === 503) {
            this.errorMessage =
              'The AI service is currently unavailable. ' +
              'Make sure Ollama is running.';
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

  logout(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}