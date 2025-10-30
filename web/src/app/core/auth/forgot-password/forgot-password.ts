import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../auth-service';

@Component({
  selector: 'app-forgot-password',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './forgot-password.html',
  styleUrls: ['./forgot-password.scss']
})
export class ForgotPassword {
  email: string = '';
  message: string = '';
  error: string = '';
  loading: boolean = false;

  constructor(
    private authService: AuthService,
    private router: Router
  ) {}

  onSubmit() {
    if (!this.email) {
      this.error = 'Por favor, informe o e-mail cadastrado.';
      return;
    }

    this.loading = true;
    this.error = '';
    this.message = '';

    this.authService.forgotPassword(this.email).subscribe({
      next: (res) => {
        this.loading = false;
        this.message = res.message || 'Código enviado com sucesso!';

        // 💾 Guarda o e-mail temporariamente para a próxima etapa
        sessionStorage.setItem('resetEmail', this.email);

        // ⏳ Redireciona para a tela de verificação do token após 2 segundos
        setTimeout(() => this.router.navigate(['/validar-codigo']), 2000);
      },
      error: (err) => {
        this.loading = false;
        this.error = err.error?.message || 'Erro ao enviar o código de redefinição.';
      }
    });
  }
}
