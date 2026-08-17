import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { environment } from '../../../environments/environment';

export interface UserResponse {
  id: number;
  name: string;
  email: string;
  role: 'OWNER' | 'MANAGER' | 'STAFF';
  active: boolean;
  lastLogin: string | null;
}

export interface UserCreateRequest {
  name: string;
  email: string;
  password: string;
  role: 'MANAGER' | 'STAFF';
}

export interface RoleUpdateRequest {
  role: 'MANAGER' | 'STAFF';
}

export interface UserStatusUpdateRequest {
  active: boolean;
}

@Injectable({
  providedIn: 'root'
})
export class UserManagementService {

  private readonly apiUrl =
    `${environment.apiUrl}/users`;

  constructor(
    private readonly http: HttpClient
  ) {}

  getUsers(): Observable<UserResponse[]> {
    return this.http.get<UserResponse[]>(
      this.apiUrl
    );
  }

  createUser(
    request: UserCreateRequest
  ): Observable<UserResponse> {
    return this.http.post<UserResponse>(
      this.apiUrl,
      request
    );
  }

  updateRole(
    userId: number,
    role: 'MANAGER' | 'STAFF'
  ): Observable<UserResponse> {
    return this.http.patch<UserResponse>(
      `${this.apiUrl}/${userId}/role`,
      { role }
    );
  }

  updateStatus(
    userId: number,
    active: boolean
  ): Observable<UserResponse> {
    return this.http.patch<UserResponse>(
      `${this.apiUrl}/${userId}/status`,
      { active }
    );
  }

  deleteUser(
    userId: number
  ): Observable<string> {
    return this.http.delete(
      `${this.apiUrl}/${userId}`,
      {
        responseType: 'text'
      }
    );
  }
}
