import { CommonModule } from '@angular/common';
import {
  ChangeDetectorRef,
  Component,
  ElementRef,
  OnDestroy,
  OnInit,
  ViewChild,
  inject
} from '@angular/core';

import {
  Chart,
  ChartConfiguration,
  registerables
} from 'chart.js';

import {
  forkJoin,
  finalize
} from 'rxjs';

import {
  AnalyticsResponse,
  DemandForecastResponse
} from '../../core/models/analytics';

import {
  Inventory
} from '../../core/models/inventory';

import {
  StockTransaction
} from '../../core/models/stock-transaction';

import {
  AnalyticsService
} from '../../core/services/analytics';

import {
  InventoryService
} from '../../core/services/inventory';

import {
  StockTransactionService
} from '../../core/services/stock-transaction';

Chart.register(...registerables);

@Component({
  selector: 'app-analytics',
  standalone: true,
  imports: [
    CommonModule
  ],
  templateUrl: './analytics.html',
  styleUrl: './analytics.css'
})
export class Analytics implements OnInit, OnDestroy {
  private readonly analyticsService =
    inject(AnalyticsService);

  private readonly inventoryService =
    inject(InventoryService);

  private readonly transactionService =
    inject(StockTransactionService);

  private readonly changeDetector =
    inject(ChangeDetectorRef);

  @ViewChild('inventoryChart')
  inventoryChartRef?: ElementRef<HTMLCanvasElement>;

  @ViewChild('movementChart')
  movementChartRef?: ElementRef<HTMLCanvasElement>;

  analytics: AnalyticsResponse | null = null;

  inventoryItems: Inventory[] = [];
  transactions: StockTransaction[] = [];

  forecasts: DemandForecastResponse[] = [];

  isLoading = true;
  isForecastLoading = false;

  errorMessage = '';

  forecastDays = 7;

  private inventoryChart?: Chart;
  private movementChart?: Chart;

  ngOnInit(): void {
    this.loadAnalytics();
  }

  ngOnDestroy(): void {
    this.inventoryChart?.destroy();
    this.movementChart?.destroy();
  }

  loadAnalytics(): void {
    this.isLoading = true;
    this.errorMessage = '';

    forkJoin({
      analytics:
        this.analyticsService.getAnalytics(),

      inventory:
        this.inventoryService.getInventory(),

      transactions:
        this.transactionService
          .getAllTransactions()
    })
      .pipe(
        finalize(() => {
          this.isLoading = false;
          this.changeDetector.detectChanges();
        })
      )
      .subscribe({
        next: response => {
          this.analytics = response.analytics;
          this.inventoryItems = response.inventory;
          this.transactions = response.transactions;

          this.changeDetector.detectChanges();

          setTimeout(() => {
            this.createCharts();
          });

          this.loadForecasts();
        },

        error: error => {
          console.error(
            'Analytics load failed:',
            error
          );

          this.errorMessage =
            error.error?.message ??
            'Unable to load analytics.';
        }
      });
  }

  loadForecasts(): void {
    if (this.inventoryItems.length === 0) {
      this.forecasts = [];
      return;
    }

    this.isForecastLoading = true;

    const forecastRequests =
      this.inventoryItems.map(item =>
        this.analyticsService.getDemandForecast(
          item.id,
          this.forecastDays
        )
      );

    forkJoin(forecastRequests)
      .pipe(
        finalize(() => {
          this.isForecastLoading = false;
          this.changeDetector.detectChanges();
        })
      )
      .subscribe({
        next: forecasts => {
          this.forecasts = forecasts.sort(
            (first, second) =>
              second.recommendedReorderQuantity -
              first.recommendedReorderQuantity
          );
        },

        error: error => {
          console.error(
            'Forecast load failed:',
            error
          );

          this.forecasts = [];
        }
      });
  }

  changeForecastDays(days: number): void {
    this.forecastDays = days;
    this.loadForecasts();
  }

  private createCharts(): void {
    this.createInventoryChart();
    this.createMovementChart();
  }

  private createInventoryChart(): void {
    const canvas =
      this.inventoryChartRef?.nativeElement;

    if (!canvas) {
      return;
    }

    this.inventoryChart?.destroy();

    const healthy =
      this.inventoryItems.filter(
        item =>
          item.stockLevel >
          item.minimumStock
      ).length;

    const low =
      this.inventoryItems.filter(
        item =>
          item.stockLevel > 0 &&
          item.stockLevel <=
            item.minimumStock
      ).length;

    const out =
      this.inventoryItems.filter(
        item => item.stockLevel === 0
      ).length;

    const configuration:
      ChartConfiguration<'doughnut'> = {
        type: 'doughnut',

        data: {
          labels: [
            'Healthy',
            'Low stock',
            'Out of stock'
          ],

          datasets: [
            {
              data: [
                healthy,
                low,
                out
              ],

              backgroundColor: [
                '#22c55e',
                '#f59e0b',
                '#ef4444'
              ],

              borderWidth: 0
            }
          ]
        },

        options: {
          responsive: true,
          maintainAspectRatio: false,

          plugins: {
            legend: {
              position: 'bottom'
            }
          }
        }
      };

    this.inventoryChart =
      new Chart(canvas, configuration);
  }

  private createMovementChart(): void {
    const canvas =
      this.movementChartRef?.nativeElement;

    if (!canvas) {
      return;
    }

    this.movementChart?.destroy();

    const grouped = new Map<
      string,
      {
        stockIn: number;
        stockOut: number;
      }
    >();

    for (
      const transaction of
      this.transactions
    ) {
      const date =
        transaction.createdAt.slice(0, 10);

      const current =
        grouped.get(date) ?? {
          stockIn: 0,
          stockOut: 0
        };

      if (
        transaction.transactionType ===
        'STOCK_IN'
      ) {
        current.stockIn +=
          transaction.quantity;
      } else {
        current.stockOut +=
          transaction.quantity;
      }

      grouped.set(date, current);
    }

    const labels =
      Array.from(grouped.keys())
        .sort()
        .slice(-14);

    const stockInValues =
      labels.map(
        label =>
          grouped.get(label)?.stockIn ?? 0
      );

    const stockOutValues =
      labels.map(
        label =>
          grouped.get(label)?.stockOut ?? 0
      );

    const configuration:
      ChartConfiguration<'line'> = {
        type: 'line',

        data: {
          labels,

          datasets: [
            {
              label: 'Stock in',
              data: stockInValues,
              borderColor: '#16a34a',
              backgroundColor:
                'rgba(22, 163, 74, 0.12)',
              tension: 0.3,
              fill: true
            },
            {
              label: 'Stock out',
              data: stockOutValues,
              borderColor: '#dc2626',
              backgroundColor:
                'rgba(220, 38, 38, 0.08)',
              tension: 0.3,
              fill: true
            }
          ]
        },

        options: {
          responsive: true,
          maintainAspectRatio: false,

          scales: {
            y: {
              beginAtZero: true
            }
          }
        }
      };

    this.movementChart =
      new Chart(canvas, configuration);
  }

  get highRiskForecasts():
    DemandForecastResponse[] {
    return this.forecasts.filter(
      forecast =>
        forecast.stockRisk
          .toLowerCase()
          .includes('high') ||
        forecast.recommendedReorderQuantity > 0
    );
  }
}
