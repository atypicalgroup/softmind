import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { AuthService } from '../auth/auth-service';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const authService = inject(AuthService);
  const token = authService.getToken();

  // ⚠️ Ignorar rotas públicas
  const publicRoutes = [
    '/auth/login',
    '/auth/register',
    '/auth/forgot-password',
    '/auth/verify-token',
    '/auth/change-password'
  ];

  // Se a URL contém uma rota pública → não adiciona token
  if (publicRoutes.some(route => req.url.includes(route))) {
    return next(req);
  }

  // Se há token → clona e adiciona o header Authorization
  if (token) {
    const cloned = req.clone({
      setHeaders: {
        Authorization: `Bearer ${token}`
      }
    });
    return next(cloned);
  }

  // Sem token → segue normal
  return next(req);
};
