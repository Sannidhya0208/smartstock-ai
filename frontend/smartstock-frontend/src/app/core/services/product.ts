import { HttpClient, HttpParams } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';

import { environment } from '../../../environments/environment';
import { Product } from '../models/product';
import { ProductRequest } from '../models/product-request';
import {
  ProductPageResponse
} from '../models/product-page-response';

@Injectable({
  providedIn: 'root'
})
export class ProductService {
  private readonly http = inject(HttpClient);

  private readonly apiUrl =
    `${environment.apiUrl}/products`;

  getProducts(
    page = 0,
    size = 10,
    sortBy = 'id',
    sortDirection = 'asc',
    search = ''
  ): Observable<ProductPageResponse> {
    let params = new HttpParams()
      .set('page', page)
      .set('size', size)
      .set('sortBy', sortBy)
      .set('sortDirection', sortDirection);

    if (search.trim()) {
      params = params.set('search', search.trim());
    }

    return this.http.get<ProductPageResponse>(
      `${this.apiUrl}/page`,
      { params }
    );
  }

  getProductById(id: number): Observable<Product> {
    return this.http.get<Product>(
      `${this.apiUrl}/${id}`
    );
  }

  createProduct(
    request: ProductRequest
  ): Observable<Product> {
    return this.http.post<Product>(
      this.apiUrl,
      request
    );
  }

  updateProduct(
    id: number,
    request: ProductRequest
  ): Observable<Product> {
    return this.http.put<Product>(
      `${this.apiUrl}/${id}`,
      request
    );
  }

  deleteProduct(id: number): Observable<string> {
    return this.http.delete(
      `${this.apiUrl}/${id}`,
      {
        responseType: 'text'
      }
    );
  }
}