import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface ChatbotRequest {
  message: string;
  context?: string;
}

export interface ChatbotResponse {
  response: string;
  context: string;
  timestamp: number;
}

@Injectable({
  providedIn: 'root'
})
export class ChatbotService {
  private apiUrl = 'http://localhost:8080/api/chatbot';

  constructor(private http: HttpClient) { }

  ask(request: ChatbotRequest): Observable<ChatbotResponse> {
    return this.http.post<ChatbotResponse>(`${this.apiUrl}/ask`, request);
  }
}
