import { Component } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../auth-service';
import { FormsModule } from "@angular/forms";
import { NgOptimizedImage } from "@angular/common";

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [FormsModule, NgOptimizedImage, RouterLink],
  templateUrl: './login.html',
  styleUrl: './login.scss'
})
export class Login {
  username = '';
  password = '';
  loading = false;

  constructor(private authService: AuthService, private router: Router) {}

  onSubmit(): void {
    if (!this.username.trim() || !this.password.trim()) {
      alert('Por favor, preencha usuário e senha.');
      return;
    }

    this.loading = true;

    this.authService.login(this.username, this.password).subscribe({
      next: (user) => {
        console.log('✅ Login bem-sucedido:', user);
        this.loading = false;

        // Verifica se veio o companyId
        if (!user.companyId) {
          console.warn('⚠️ companyId ausente na resposta. Verifique o backend.');
        }

        // Redireciona para o dashboard
        this.router.navigate(['/portal/dashboard']);
      },
      error: (err) => {
        this.loading = false;
        console.error('❌ Erro no login:', err);
        alert(err.error?.message || 'Credenciais inválidas!');
      }
    });
  }
}
