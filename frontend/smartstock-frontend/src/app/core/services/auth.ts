import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { environment } from '../../../environments/environment';
import { LoginRequest } from '../models/login-request';
import { AuthResponse } from '../models/auth-response';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private readonly http = inject(HttpClient);
  private readonly apiUrl = `${environment.apiUrl}/auth`;
  private readonly tokenKey = 'smartstock_token';
  private readonly userKey = 'smartstock_user';

  login(request: LoginRequest): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(
      `${this.apiUrl}/login`,
      request
    );
  }

  saveSession(response: AuthResponse): void {
    localStorage.setItem(
      this.tokenKey,
      response.token
    );

    localStorage.setItem(
      this.userKey,
      JSON.stringify({
        email: response.email,
        role: response.role,
        companyId: response.companyId,
        companyName: response.companyName
      })
    );
  }

  getToken(): string | null {
    return localStorage.getItem(this.tokenKey);
  }

  isAuthenticated(): boolean {
    return Boolean(this.getToken());
  }

  logout(): void {
    localStorage.removeItem(this.tokenKey);
    localStorage.removeItem(this.userKey);

  }
  getCurrentUser(): {
    email: string;
    role: string;
    companyId: number;
    companyName: string;
  } | null {

    const user = localStorage.getItem(this.userKey);

    if (!user) {
      return null;
    }

    try {
      return JSON.parse(user);
    } catch {
      return null;
    }
  }

  getRole(): string | null {
    return this.getCurrentUser()?.role ?? null;
  }

  isOwner(): boolean {
    return this.getRole() === 'OWNER';
  }

  isManager(): boolean {
    return this.getRole() === 'MANAGER';
  }

  isStaff(): boolean {
    return this.getRole() === 'STAFF';
  }

  getCompanyId(): number | null {
    return this.getCurrentUser()?.companyId ?? null;
  }

  getCompanyName(): string | null {
    return this.getCurrentUser()?.companyName ?? null;
  }

}
