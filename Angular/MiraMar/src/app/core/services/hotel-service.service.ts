import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { HotelService } from '../models/entities';

@Injectable({
  providedIn: 'root'
})
export class HotelServiceService {

  private baseUrl = 'http://localhost:8090/services';

  constructor(private http: HttpClient) {}

  findAll(): Observable<HotelService[]> {
    return this.http.get<HotelService[]>(`${this.baseUrl}/all`);
  }

  findById(id: number): Observable<HotelService> {
    return this.http.get<HotelService>(`${this.baseUrl}/find/${id}`);
  }

  create(servicio: Omit<HotelService, 'id'>): Observable<HotelService> {
    return this.http.post<HotelService>(`${this.baseUrl}/add`, servicio);
  }

  update(id: number, servicio: Omit<HotelService, 'id'>): Observable<HotelService> {
    return this.http.put<HotelService>(`${this.baseUrl}/update/${id}`, servicio);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/delete/${id}`);
  }
}
