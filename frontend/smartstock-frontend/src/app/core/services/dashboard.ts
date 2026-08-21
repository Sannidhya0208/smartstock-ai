import {
  inject,
  Injectable
} from '@angular/core';

import {
  HttpClient
} from '@angular/common/http';

import {
  Observable
} from 'rxjs';

import {
  environment
} from '../../../environments/environment';

import {
  DashboardResponse
} from '../models/dashboard-response';

import {
  AiDashboardResponse
} from '../models/ai-dashboard-response';

@Injectable({
  providedIn: 'root'
})
export class DashboardService {

  private readonly http =
    inject(HttpClient);

  private readonly dashboardUrl =
    `${environment.apiUrl}/dashboard`;

  private readonly aiDashboardUrl =
    `${environment.apiUrl}/ai/dashboard-summary`;

  getDashboard():
    Observable<DashboardResponse> {

    console.log(
      'Calling:',
      this.dashboardUrl
    );

    return this.http.get<DashboardResponse>(
      this.dashboardUrl
    );
  }

  getAiDashboardSummary():
    Observable<AiDashboardResponse> {

    return this.http.get<AiDashboardResponse>(
      this.aiDashboardUrl
    );
  }
}