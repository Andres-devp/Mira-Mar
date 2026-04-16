import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { Operator } from '../models/entities';

@Injectable({
  providedIn: 'root'
})
export class OperatorService {

  private apiUrl = 'http://localhost:8080/operator';

  constructor(private http: HttpClient) {}

  getAll(): Observable<Operator[]> {
    return this.http.get<Operator[]>(`${this.apiUrl}/all`).pipe(
      catchError(this.handleError)
    );
  }

  getById(id: number): Observable<Operator> {
    return this.http.get<Operator>(`${this.apiUrl}/${id}`).pipe(
      catchError(this.handleError)
    );
  }

  create(operador: Operator): Observable<Operator> {
    return this.http.post<Operator>(`${this.apiUrl}/add`, operador).pipe(
      catchError(this.handleError)
    );
  }

  update(id: number, operador: Operator): Observable<Operator> {
    return this.http.put<Operator>(`${this.apiUrl}/${id}`, operador).pipe(
      catchError(this.handleError)
    );
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`).pipe(
      catchError(this.handleError)
    );
  }

  private handleError(error: any): Observable<never> {
    console.error('OperatorService error:', error);
    return throwError(() => error);
  }
}
