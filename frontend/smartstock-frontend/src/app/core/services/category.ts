import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { environment } from '../../../environments/environment';
import {
  Category,
  CategoryRequest
} from '../models/category';

@Injectable({
  providedIn: 'root'
})
export class CategoryService {

  private readonly http = inject(HttpClient);

  private readonly apiUrl =
    `${environment.apiUrl}/categories`;

  getCategories(): Observable<Category[]> {
    return this.http.get<Category[]>(this.apiUrl);
  }

  getCategoryById(id: number): Observable<Category> {
    return this.http.get<Category>(
      `${this.apiUrl}/${id}`
    );
  }

  createCategory(
    request: CategoryRequest
  ): Observable<Category> {
    return this.http.post<Category>(
      this.apiUrl,
      request
    );
  }

  updateCategory(
    id: number,
    request: CategoryRequest
  ): Observable<Category> {
    return this.http.put<Category>(
      `${this.apiUrl}/${id}`,
      request
    );
  }

  deleteCategory(id: number): Observable<void> {
    return this.http.delete<void>(
      `${this.apiUrl}/${id}`
    );
  }
}