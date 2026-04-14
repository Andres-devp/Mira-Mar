import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface LoginRequest {
  username: string;
  password: string;
}

export interface LoginResponse {
  id: number;
  rol: string;
}

export interface RegisterRequest {
  nombre: string;
  usuario: string;
  email: string;
  contrasena: string;
  contrasenaConfirm: string;
}

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private baseUrl = 'http://localhost:8090/auth';

  private currentUserId: number | null = null;
  private currentUserRole: string | null = null;

  constructor(private http: HttpClient) {}

  login(credentials: LoginRequest): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(`${this.baseUrl}/login`, credentials);
  }

  register(data: RegisterRequest): Observable<any> {
    return this.http.post(`${this.baseUrl}/register`, data);
  }

  saveSession(user: LoginResponse): void {
    this.currentUserId = user.id;
    this.currentUserRole = user.rol;
  }

  logout(): void {
    this.currentUserId = null;
    this.currentUserRole = null;
  }

  isLoggedIn(): boolean {
    return this.currentUserId !== null;
  }

  getUserId(): number | null {
    return this.currentUserId;
  }

  getUserRole(): string | null {
    return this.currentUserRole;
  }

  isAdmin(): boolean {
    return this.currentUserRole === 'ADMIN';
  }

  isOperator(): boolean {
    return this.currentUserRole === 'OPERATOR';
  }
}
