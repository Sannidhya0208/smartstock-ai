import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { environment } from '../../../environments/environment';
import {
  AnalyticsResponse,
  DemandForecastResponse
} from '../models/analytics';

@Injectable({
  providedIn: 'root'
})
export class AnalyticsService {
  private readonly http = inject(HttpClient);

  private readonly analyticsUrl =
    `${environment.apiUrl}/analytics`;

  private readonly forecastUrl =
    `${environment.apiUrl}/forecast`;

  getAnalytics(): Observable<AnalyticsResponse> {
    return this.http.get<AnalyticsResponse>(
      this.analyticsUrl
    );
  }

  getDemandForecast(
    inventoryId: number,
    days = 7
  ): Observable<DemandForecastResponse> {
    return this.http.get<DemandForecastResponse>(
      `${this.forecastUrl}/inventory/${inventoryId}`,
      {
        params: {
          days
        }
      }
    );
  }
}
