import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface SupportPoint {
  id?: string;
  name: string;
  description: string;
  contactNumber: string[];
}

@Injectable({
  providedIn: 'root'
})
export class SupportService {
  private readonly API_URL = 'http://91.108.126.24:8080/supports'; // ajuste conforme backend

  constructor(private http: HttpClient) {}

  getAll(): Observable<SupportPoint[]> {
    return this.http.get<SupportPoint[]>(this.API_URL);
  }

  create(point: SupportPoint): Observable<SupportPoint> {
    return this.http.post<SupportPoint>(this.API_URL, point);
  }

  update(id: string, point: SupportPoint): Observable<SupportPoint> {
    return this.http.put<SupportPoint>(`${this.API_URL}/${id}`, point);
  }

  delete(id: string): Observable<void> {
    return this.http.delete<void>(`${this.API_URL}/${id}`);
  }
}
