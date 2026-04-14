import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Client } from '../models/entities';

@Injectable({
  providedIn: 'root'
})
export class ClientService {

  private baseUrl = 'http://localhost:8090/usuarios';

  constructor(private http: HttpClient) {}

  findAll(): Observable<Client[]> {
    return this.http.get<Client[]>(`${this.baseUrl}/all`);
  }

  findById(id: number): Observable<Client> {
    return this.http.get<Client>(`${this.baseUrl}/find/${id}`);
  }

  create(client: Client): Observable<Client> {
    return this.http.post<Client>(`${this.baseUrl}/add`, client);
  }

  update(id: number, client: Client): Observable<Client> {
    return this.http.put<Client>(`${this.baseUrl}/update/${id}`, client);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/delete/${id}`);
  }
}
