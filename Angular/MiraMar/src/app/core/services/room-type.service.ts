import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { RoomType } from '../models/entities';

@Injectable({
  providedIn: 'root'
})
export class RoomTypeService {

  private baseUrl = 'http://localhost:8090/roomtypes';

  constructor(private http: HttpClient) {}

  findAll(): Observable<RoomType[]> {
    return this.http.get<RoomType[]>(`${this.baseUrl}/all`);
  }

  findById(id: number): Observable<RoomType> {
    return this.http.get<RoomType>(`${this.baseUrl}/find/${id}`);
  }

  filter(capacidad?: number, precioMax?: number): Observable<RoomType[]> {
    let params: any = {};
    if (capacidad != null) params.capacidad = capacidad;
    if (precioMax != null) params.precioMax = precioMax;
    return this.http.get<RoomType[]>(`${this.baseUrl}/filter`, { params });
  }

  create(tipo: Omit<RoomType, 'id'>): Observable<RoomType> {
    return this.http.post<RoomType>(`${this.baseUrl}/add`, tipo);
  }

  update(id: number, tipo: Omit<RoomType, 'id'>): Observable<RoomType> {
    return this.http.put<RoomType>(`${this.baseUrl}/update/${id}`, tipo);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/delete/${id}`);
  }
}
