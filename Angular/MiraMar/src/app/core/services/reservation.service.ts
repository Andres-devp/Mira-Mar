import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Reservation } from '../models/entities';

@Injectable({
  providedIn: 'root'
})
export class ReservationService {

  private baseUrl = 'http://localhost:8090/reservations';

  constructor(private http: HttpClient) {}

  findAll(): Observable<Reservation[]> {
    return this.http.get<Reservation[]>(`${this.baseUrl}/all`);
  }

  findById(id: number): Observable<Reservation> {
    return this.http.get<Reservation>(`${this.baseUrl}/find/${id}`);
  }

  create(reservation: Reservation): Observable<Reservation> {
    return this.http.post<Reservation>(`${this.baseUrl}/add`, reservation);
  }

  update(id: number, reservation: Reservation): Observable<Reservation> {
    return this.http.put<Reservation>(`${this.baseUrl}/update/${id}`, reservation);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/delete/${id}`);
  }
}
