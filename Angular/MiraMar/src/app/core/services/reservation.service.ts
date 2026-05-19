import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { Account, Reservation, ReservationCreateRequest } from '../models/entities';

@Injectable({
  providedIn: 'root'
})
export class ReservationService {

  private apiUrl = 'http://localhost:8080/reservations';

  constructor(private http: HttpClient) {}

  getAll(): Observable<Reservation[]> {
    return this.http.get<Reservation[]>(`${this.apiUrl}/all`).pipe(
      catchError(this.handleError)
    );
  }

  getById(id: number): Observable<Reservation> {
    return this.http.get<Reservation>(`${this.apiUrl}/${id}`).pipe(
      catchError(this.handleError)
    );
  }

  create(request: ReservationCreateRequest): Observable<Reservation> {
    return this.http.post<Reservation>(`${this.apiUrl}/add`, request).pipe(
      catchError(this.handleError)
    );
  }

  update(id: number, reservation: Reservation): Observable<Reservation> {
    return this.http.put<Reservation>(`${this.apiUrl}/${id}`, reservation).pipe(
      catchError(this.handleError)
    );
  }

  updateEstado(id: number, estado: string): Observable<Reservation> {
    return this.http.put<Reservation>(`${this.apiUrl}/${id}/status`, { estado }).pipe(
      catchError(this.handleError)
    );
  }

  getByClient(clientId: number): Observable<Reservation[]> {
  return this.http.get<Reservation[]>(`${this.apiUrl}/client/${clientId}`).pipe(
    catchError(this.handleError)
  );
}

  updateReservacion(id: number, roomTypeId: number, fechaInicio: string, fechaFin: string, cantidadPersonas: number): Observable<Reservation> {
    return this.http.put<Reservation>(`${this.apiUrl}/${id}/update`, {
      roomTypeId,
      fechaInicio,
      fechaFin,
      cantidadPersonas
    }).pipe(catchError(this.handleError));
  }

  getAccount(id: number): Observable<Account> {
    return this.http.get<Account>(`${this.apiUrl}/${id}/account`).pipe(
      catchError(this.handleError)
    );
  }

  payAccount(id: number): Observable<Account> {
    return this.http.post<Account>(`${this.apiUrl}/${id}/pay`, {}).pipe(
      catchError(this.handleError)
    );
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`).pipe(
      catchError(this.handleError)
    );
  }

  private handleError(error: any): Observable<never> {
    console.error('ReservationService error:', error);
    return throwError(() => error);
  }
}
