import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, of, throwError } from 'rxjs';
import { catchError, tap } from 'rxjs/operators';
import {
  LoginRequest,
  LoginResponse,
  RegisterRequest,
  RegisterResponse,
} from '../models/entities';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private apiUrl = 'http://localhost:8080/auth';
  private sessionStorageKey = 'miramar_session';

  constructor(private http: HttpClient) {}

  login(credentials: LoginRequest): Observable<LoginResponse> {
    return this.http
      .post<LoginResponse>(`${this.apiUrl}/login`, credentials)
      .pipe(
        tap(response => this.setSession(response)),
        catchError(this.handleError)
      );
  }

  register(data: RegisterRequest): Observable<RegisterResponse> {
    return this.http
      .post<RegisterResponse>(`${this.apiUrl}/register`, data)
      .pipe(catchError(this.handleError));
  }

  setSession(session: LoginResponse): void {
    localStorage.setItem(this.sessionStorageKey, JSON.stringify(session));
  }

  getSession(): LoginResponse | null {
    const raw = localStorage.getItem(this.sessionStorageKey);
    if (!raw) {
      return null;
    }

    try {
      return JSON.parse(raw) as LoginResponse;
    } catch {
      this.clearSession();
      return null;
    }
  }

  getToken(): string | null {
    const session = this.getSession();
    return session?.accessToken || null;
  }

  getUserId(): number | null {
    const session = this.getSession();
    return session?.userId || null;
  }

  getRole(): string | null {
    const session = this.getSession();
    return session?.role || null;
  }

  isLoggedIn(): boolean {
    return this.getSession() !== null && this.getToken() !== null;
  }

  clearSession(): void {
    localStorage.removeItem(this.sessionStorageKey);
  }

  logout(): Observable<void> {
    return this.http.post<void>(`${this.apiUrl}/logout`, {}).pipe(
      tap(() => this.clearSession()),
      catchError(() => {
        this.clearSession();
        return of(void 0);
      }),
    );
  }

  private handleError(error: any): Observable<never> {
    console.error('AuthService error:', error);
    return throwError(() => error);
  }
}

