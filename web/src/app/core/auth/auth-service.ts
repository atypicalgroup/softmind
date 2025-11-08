import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable } from 'rxjs';
import { tap } from 'rxjs/operators';

interface User {
  id: string;
  name: string;
  username: string;
  token: string;
  email?: string;
  role?: string;
  phone?: string;
  companyId: string;
  alreadyAnswered?: boolean;
}

interface PasswordResetResponse {
  message: string;
}

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiUrl = 'http://91.108.126.24:8080/auth';

  private userSubject = new BehaviorSubject<User | null>(null);
  user$ = this.userSubject.asObservable();

  constructor(private http: HttpClient) {}

  // ==================================================
  // 🔐 LOGIN / LOGOUT / USUÁRIO ATUAL
  // ==================================================
  login(username: string, password: string): Observable<User> {
    return this.http.post<User>(`${this.apiUrl}/login`, { username, password }).pipe(
      tap(user => {
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
  getToken(): string | null {
    const user = this.getUser();
    return user?.token ?? null;
  }

  // ==================================================
  // 🔁 RECUPERAÇÃO DE SENHA
  // ==================================================

  /**
   * 1️⃣ Envia e-mail de redefinição de senha
   */
  forgotPassword(email: string): Observable<PasswordResetResponse> {
    return this.http.post<PasswordResetResponse>(`${this.apiUrl}/forgot-password`, { email });
  }

  /**
   * 2️⃣ Valida token de 6 dígitos recebido por e-mail
   */
  verifyToken(email: string, token: string): Observable<PasswordResetResponse> {
    return this.http.post<PasswordResetResponse>(`${this.apiUrl}/verify-token`, { email, token });
  }

  /**
   * 3️⃣ Altera senha com token válido
   */
  changePassword(email: string, token: string, newPassword: string): Observable<PasswordResetResponse> {
    return this.http.post<PasswordResetResponse>(`${this.apiUrl}/change-password`, { email, token, newPassword });
  }
}
