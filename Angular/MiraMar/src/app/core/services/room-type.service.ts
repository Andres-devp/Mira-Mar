import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { RoomType } from '../models/entities';

@Injectable({
  providedIn: 'root'
})
export class RoomTypeService {

  private apiUrl = 'http://localhost:8080/roomtypes';

  constructor(private http: HttpClient) {}

  getAll(): Observable<RoomType[]> {
    return this.http.get<RoomType[]>(`${this.apiUrl}/all`).pipe(
      catchError(this.handleError)
    );
  }

  filter(capacidad?: number, precioMax?: number): Observable<RoomType[]> {
    let params = new HttpParams();
    if (capacidad !== undefined && capacidad !== null) {
      params = params.set('capacidad', capacidad.toString());
    }
    if (precioMax !== undefined && precioMax !== null) {
      params = params.set('precioMax', precioMax.toString());
    }
    return this.http.get<RoomType[]>(`${this.apiUrl}/filter`, { params }).pipe(
      catchError(this.handleError)
    );
  }

  getById(id: number): Observable<RoomType> {
    return this.http.get<RoomType>(`${this.apiUrl}/${id}`).pipe(
      catchError(this.handleError)
    );
  }

  create(tipo: RoomType): Observable<RoomType> {
    return this.http.post<RoomType>(`${this.apiUrl}/add`, tipo).pipe(
      catchError(this.handleError)
    );
  }

  update(id: number, tipo: RoomType): Observable<RoomType> {
    return this.http.put<RoomType>(`${this.apiUrl}/${id}`, tipo).pipe(
      catchError(this.handleError)
    );
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`).pipe(
      catchError(this.handleError)
    );
  }

  private handleError(error: any): Observable<never> {
    console.error('RoomTypeService error:', error);
    return throwError(() => error);
  }
}
