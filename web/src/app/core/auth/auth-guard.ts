import { Injectable } from '@angular/core';
import { CanActivate, Router, UrlTree } from '@angular/router';
import { AuthService } from './auth-service';

@Injectable({ providedIn: 'root' })
export class AuthGuard implements CanActivate {
  constructor(private authService: AuthService, private router: Router) {}

  canActivate(): boolean | UrlTree {
    const user = this.authService.getUser();
    const token = this.authService.getToken();

    // 🔹 Se não estiver logado, redireciona para login
    if (!user || !token) {
      this.authService.logout();
      return this.router.createUrlTree(['/login']);
    }

    // 🔹 Token válido → permite acesso
    return true;
  }
}
