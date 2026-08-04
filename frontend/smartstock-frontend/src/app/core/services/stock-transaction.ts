import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { environment } from '../../../environments/environment';
import {
  StockTransaction
} from '../models/stock-transaction';

@Injectable({
  providedIn: 'root'
})
export class StockTransactionService {
  private readonly http = inject(HttpClient);

  private readonly apiUrl =
    `${environment.apiUrl}/transactions`;

  getAllTransactions(): Observable<StockTransaction[]> {
    return this.http.get<StockTransaction[]>(
      this.apiUrl
    );
  }

  getProductTransactions(
    productId: number
  ): Observable<StockTransaction[]> {
    return this.http.get<StockTransaction[]>(
      `${this.apiUrl}/product/${productId}`
    );
  }
}
