import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { HotelService } from '../models/entities';

@Injectable({
  providedIn: 'root'
})
export class HotelServiceService {

  private apiUrl = 'http://localhost:8080/services';

  constructor(private http: HttpClient) {}

  getAll(): Observable<HotelService[]> {
    return this.http.get<HotelService[]>(`${this.apiUrl}/all`).pipe(
      catchError(this.handleError)
    );
  }

  getById(id: number): Observable<HotelService> {
    return this.http.get<HotelService>(`${this.apiUrl}/${id}`).pipe(
      catchError(this.handleError)
    );
  }

  create(servicio: HotelService): Observable<HotelService> {
    return this.http.post<HotelService>(`${this.apiUrl}/add`, servicio).pipe(
      catchError(this.handleError)
    );
  }

  update(id: number, servicio: HotelService): Observable<HotelService> {
    return this.http.put<HotelService>(`${this.apiUrl}/${id}`, servicio).pipe(
      catchError(this.handleError)
    );
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`).pipe(
      catchError(this.handleError)
    );
  }

  private handleError(error: any): Observable<never> {
    console.error('HotelServiceService error:', error);
    return throwError(() => error);
  }
}
