import { CommonModule } from '@angular/common';
import {
  ChangeDetectorRef,
  Component,
  OnInit,
  inject
} from '@angular/core';

import {
  StockTransaction
} from '../../core/models/stock-transaction';

import {
  StockTransactionService
} from '../../core/services/stock-transaction';

@Component({
  selector: 'app-transactions',
  standalone: true,
  imports: [
    CommonModule
  ],
  templateUrl: './transactions.html',
  styleUrl: './transactions.css'
})
export class Transactions implements OnInit {
  private readonly transactionService =
    inject(StockTransactionService);

  private readonly changeDetector =
    inject(ChangeDetectorRef);

  transactions: StockTransaction[] = [];
  filteredTransactions: StockTransaction[] = [];

  isLoading = true;
  errorMessage = '';

  searchValue = '';
  selectedType = 'ALL';

  ngOnInit(): void {
    this.loadTransactions();
  }

  loadTransactions(): void {
    this.isLoading = true;
    this.errorMessage = '';

    this.transactionService
      .getAllTransactions()
      .subscribe({
        next: response => {
          this.transactions = [...response]
            .sort((first, second) =>
              new Date(second.createdAt).getTime() -
              new Date(first.createdAt).getTime()
            );

          this.applyFilters();
          this.isLoading = false;
          this.changeDetector.detectChanges();
        },

        error: error => {
          console.error(
            'Unable to load transactions:',
            error
          );

          this.errorMessage =
            error.error?.message ??
            'Unable to load stock transactions.';

          this.isLoading = false;
          this.changeDetector.detectChanges();
        }
      });
  }

  search(event: Event): void {
    const input =
      event.target as HTMLInputElement;

    this.searchValue = input.value;
    this.applyFilters();
  }

  setTypeFilter(type: string): void {
    this.selectedType = type;
    this.applyFilters();
  }

  applyFilters(): void {
    const search =
      this.searchValue.trim().toLowerCase();

    this.filteredTransactions =
      this.transactions.filter(transaction => {
        const matchesSearch =
          !search ||
          transaction.productName
            .toLowerCase()
            .includes(search) ||
          transaction.productId
            .toString()
            .includes(search);

        const matchesType =
          this.selectedType === 'ALL' ||
          transaction.transactionType ===
            this.selectedType;

        return matchesSearch && matchesType;
      });
  }

  get stockInCount(): number {
    return this.transactions.filter(
      transaction =>
        transaction.transactionType ===
          'STOCK_IN'
    ).length;
  }

  get stockOutCount(): number {
    return this.transactions.filter(
      transaction =>
        transaction.transactionType ===
          'STOCK_OUT'
    ).length;
  }

  get totalUnitsIn(): number {
    return this.transactions
      .filter(
        transaction =>
          transaction.transactionType ===
            'STOCK_IN'
      )
      .reduce(
        (total, transaction) =>
          total + transaction.quantity,
        0
      );
  }

  get totalUnitsOut(): number {
    return this.transactions
      .filter(
        transaction =>
          transaction.transactionType ===
            'STOCK_OUT'
      )
      .reduce(
        (total, transaction) =>
          total + transaction.quantity,
        0
      );
  }
}
