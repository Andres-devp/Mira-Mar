import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { Client } from '../models/entities';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private apiUrl = '//localhost:8080/api/usuarios';

  constructor(private http: HttpClient) {}

  getAll(): Observable<Client[]> {
    return this.http.get<Client[]>(this.apiUrl).pipe(
      catchError(this.handleError)
    );
  }

  getById(id: number): Observable<Client> {
    return this.http.get<Client>(`${this.apiUrl}/${id}`).pipe(
      catchError(this.handleError)
    );
  }

  create(cliente: Client): Observable<Client> {
    return this.http.post<Client>(`${this.apiUrl}/add`, cliente).pipe(
      catchError(this.handleError)
    );
  }

  update(id: number, cliente: Client): Observable<Client> {
    return this.http.put<Client>(`${this.apiUrl}/${id}`, cliente).pipe(
      catchError(this.handleError)
    );
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`).pipe(
      catchError(this.handleError)
    );
  }

  private handleError(error: any): Observable<never> {
    console.error('UserService error:', error);
    return throwError(() => error);
  }
}
