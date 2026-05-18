import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface TranslationRequest {
  text: string;
  sourceLanguage: string;
  targetLanguage: string;
}

export interface TranslationResponse {
  originalText: string;
  translatedText: string;
  sourceLanguage: string;
  targetLanguage: string;
}

@Injectable({
  providedIn: 'root'
})
export class TranslationService {
  private apiUrl = 'http://localhost:8080/api/translation';

  constructor(private http: HttpClient) { }

  translate(request: TranslationRequest): Observable<TranslationResponse> {
    return this.http.post<TranslationResponse>(`${this.apiUrl}/translate`, request);
  }

  toggleLanguage(text: string, currentLanguage: string): Observable<TranslationResponse> {
    const targetLanguage = currentLanguage === 'es' ? 'en' : 'es';
    return this.translate({
      text: text,
      sourceLanguage: currentLanguage,
      targetLanguage: targetLanguage
    });
  }
}
