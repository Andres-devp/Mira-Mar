import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Room } from '../models/entities';

@Injectable({
  providedIn: 'root'
})
export class RoomService {

  private baseUrl = 'http://localhost:8090/rooms';

  constructor(private http: HttpClient) {}

  findAll(): Observable<Room[]> {
    return this.http.get<Room[]>(`${this.baseUrl}/all`);
  }

  findById(id: number): Observable<Room> {
    return this.http.get<Room>(`${this.baseUrl}/find/${id}`);
  }

  create(room: Room): Observable<Room> {
    return this.http.post<Room>(`${this.baseUrl}/add`, room);
  }

  update(id: number, room: Room): Observable<Room> {
    return this.http.put<Room>(`${this.baseUrl}/update/${id}`, room);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/delete/${id}`);
  }
}
