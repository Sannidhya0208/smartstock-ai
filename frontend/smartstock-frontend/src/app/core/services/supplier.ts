import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { environment } from '../../../environments/environment';
import {
  Supplier,
  SupplierRequest
} from '../models/supplier';

@Injectable({
  providedIn: 'root'
})
export class SupplierService {

  private readonly http = inject(HttpClient);

  private readonly apiUrl =
    `${environment.apiUrl}/suppliers`;

  getSuppliers(): Observable<Supplier[]> {
    return this.http.get<Supplier[]>(this.apiUrl);
  }

  getSupplierById(id: number): Observable<Supplier> {
    return this.http.get<Supplier>(
      `${this.apiUrl}/${id}`
    );
  }

  createSupplier(
    request: SupplierRequest
  ): Observable<Supplier> {
    return this.http.post<Supplier>(
      this.apiUrl,
      request
    );
  }

  updateSupplier(
    id: number,
    request: SupplierRequest
  ): Observable<Supplier> {
    return this.http.put<Supplier>(
      `${this.apiUrl}/${id}`,
      request
    );
  }

  deleteSupplier(id: number): Observable<void> {
    return this.http.delete<void>(
      `${this.apiUrl}/${id}`
    );
  }
}