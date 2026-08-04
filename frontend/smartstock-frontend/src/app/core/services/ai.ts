import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';

import { environment } from '../../../environments/environment';

import {
  AiChatRequest,
  AiChatResponse
} from '../models/ai-chat';

@Injectable({
  providedIn: 'root'
})
export class AiService {

  private readonly http =
    inject(HttpClient);

  private readonly apiUrl =
    `${environment.apiUrl}/ai`;

  askQuestion(
    question: string
  ): Observable<AiChatResponse> {

    const request: AiChatRequest = {
      question
    };

    return this.http.post<AiChatResponse>(
      `${this.apiUrl}/chat`,
      request
    );
  }
}
