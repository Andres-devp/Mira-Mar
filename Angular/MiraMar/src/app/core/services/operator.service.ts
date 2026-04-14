import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Operator } from '../models/entities';

@Injectable({
  providedIn: 'root'
})
export class OperatorServiceService {

  private baseUrl = 'http://localhost:8090/operator';

  constructor(private http: HttpClient) {}

  findAll(): Observable<Operator[]> {
    return this.http.get<Operator[]>(`${this.baseUrl}/all`);
  }

  findById(id: number): Observable<Operator> {
    return this.http.get<Operator>(`${this.baseUrl}/find/${id}`);
  }

  create(operator: Operator): Observable<Operator> {
    return this.http.post<Operator>(`${this.baseUrl}/add`, operator);
  }

  update(id: number, operator: Operator): Observable<Operator> {
    return this.http.put<Operator>(`${this.baseUrl}/update/${id}`, operator);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/delete/${id}`);
  }
}
