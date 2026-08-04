import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { environment } from '../../../environments/environment';
import {
  Inventory,
  InventoryRequest,
  StockRequest,
  StockResponse
} from '../models/inventory';

@Injectable({
  providedIn: 'root'
})
export class InventoryService {
  private readonly http = inject(HttpClient);

  private readonly apiUrl =
    `${environment.apiUrl}/inventory`;

  getInventory(): Observable<Inventory[]> {
    return this.http.get<Inventory[]>(this.apiUrl);
  }

  getInventoryById(id: number): Observable<Inventory> {
    return this.http.get<Inventory>(
      `${this.apiUrl}/${id}`
    );
  }

  createInventory(
    request: InventoryRequest
  ): Observable<Inventory> {
    return this.http.post<Inventory>(
      this.apiUrl,
      request
    );
  }

  stockIn(
    inventoryId: number,
    quantity: number
  ): Observable<StockResponse> {
    const request: StockRequest = {
      quantity
    };

    return this.http.post<StockResponse>(
      `${this.apiUrl}/${inventoryId}/stock-in`,
      request
    );
  }

  stockOut(
    inventoryId: number,
    quantity: number
  ): Observable<StockResponse> {
    const request: StockRequest = {
      quantity
    };

    return this.http.post<StockResponse>(
      `${this.apiUrl}/${inventoryId}/stock-out`,
      request
    );
  }

  deleteInventory(id: number): Observable<string> {
    return this.http.delete(
      `${this.apiUrl}/${id}`,
      {
        responseType: 'text'
      }
    );
  }
}
