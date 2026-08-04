import { Component } from '@angular/core';
import {
  Router,
  RouterLink,
  RouterLinkActive
} from '@angular/router';

import { AuthService } from '../../core/services/auth';

@Component({
  selector: 'app-sidebar',
  standalone: true,
  imports: [
    RouterLink,
    RouterLinkActive
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

  logout(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}