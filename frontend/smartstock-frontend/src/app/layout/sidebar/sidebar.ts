import { Component } from '@angular/core';
import {
  Router,
  RouterLink,
  RouterLinkActive
} from '@angular/router';

import { AuthService } from '../../core/services/auth';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-sidebar',
  standalone: true,
  imports: [
    RouterLink,
    RouterLinkActive,
    CommonModule
  ],
  templateUrl: './sidebar.html',
  styleUrl: './sidebar.css'
})
export class Sidebar {

  constructor(
    private readonly authService: AuthService,
    private readonly router: Router
  ) {
  }
  getCompanyName(): string {
    return this.authService.getCompanyName() ?? 'SmartStock';
  }

  getRole(): string {
    return this.authService.getRole() ?? '';
  }
  logout(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
  isOwner(): boolean {
    return this.authService.isOwner();
  }
  canViewAnalytics(): boolean {
    return this.authService.isOwner()
      || this.authService.isManager();
  }

  canViewAi(): boolean {
    return this.authService.isOwner()
      || this.authService.isManager();
  }
}