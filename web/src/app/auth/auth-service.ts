import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable } from 'rxjs';
import { tap } from 'rxjs/operators';

interface User {
  id: string;
  name: string;
  username: string;
  token: string;
}

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiUrl = 'http://localhost:8000/auth';

  private userSubject = new BehaviorSubject<User | null>(null);
  user$ = this.userSubject.asObservable();

  constructor(private http: HttpClient) {}

  login(username: string, password: string): Observable<User>{
      return this.http.post<User>(`${this.apiUrl}/login`, { username, password}).pipe(
          tap(user =>{
            this.userSubject.next(user);
            localStorage.setItem('usuario', JSON.stringify(user));
          })
      );
  }

  logout(): void {
    this.userSubject.next(null);
    localStorage.removeItem('usuario');
  }

  getUser(): User | null {
    const usuario = this.userSubject.value;
    if (usuario) return usuario;

    const saved = localStorage.getItem('usuario');
    return saved ? JSON.parse(saved) : null;
  }
}
