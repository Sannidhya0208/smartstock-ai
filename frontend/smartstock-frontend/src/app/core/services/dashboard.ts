import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { environment } from '../../../environments/environment';
import {
  AiDashboardResponse
} from '../models/ai-dashboard-response';

@Injectable({
  providedIn: 'root'
})
export class DashboardService {
  private readonly http = inject(HttpClient);
  private readonly apiUrl =
    `${environment.apiUrl}/ai/dashboard-summary`;

  getDashboardSummary(): Observable<AiDashboardResponse> {
    return this.http.get<AiDashboardResponse>(this.apiUrl);
  }
}
