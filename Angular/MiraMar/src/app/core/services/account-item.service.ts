import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { AccountItem } from '../models/entities';

@Injectable({
  providedIn: 'root'
})
export class AccountItemService {

  private baseUrl = 'http://localhost:8080/reservations';

  constructor(private http: HttpClient) {}

  getByReservation(reservationId: number): Observable<AccountItem[]> {
    return this.http.get<AccountItem[]>(`${this.baseUrl}/${reservationId}/items`).pipe(
      catchError(this.handleError)
    );
  }

  add(reservationId: number, hotelServiceId: number, cantidad: number): Observable<AccountItem> {
    return this.http.post<AccountItem>(`${this.baseUrl}/${reservationId}/items`, {
      hotelServiceId,
      cantidad
    }).pipe(catchError(this.handleError));
  }

  updateCantidad(reservationId: number, itemId: number, cantidad: number): Observable<AccountItem> {
    return this.http.put<AccountItem>(`${this.baseUrl}/${reservationId}/items/${itemId}`, {
      cantidad
    }).pipe(catchError(this.handleError));
  }

  remove(reservationId: number, itemId: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${reservationId}/items/${itemId}`).pipe(
      catchError(this.handleError)
    );
  }

  private handleError(error: any): Observable<never> {
    console.error('AccountItemService error:', error);
    return throwError(() => error);
  }
}
