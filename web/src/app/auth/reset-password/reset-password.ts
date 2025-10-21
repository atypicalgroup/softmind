import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../auth-service';

@Component({
  selector: 'app-reset-password',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './reset-password.html',
  styleUrls: ['./reset-password.scss']
})
export class ResetPassword {
  form!: FormGroup;
  email: string = '';
  token: string = '';
  message: string = '';
  error: string = '';
  loading: boolean = false;

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router
  ) {
    this.form = this.fb.group({
      newPassword: ['', [Validators.required, Validators.minLength(6)]],
      confirmPassword: ['', [Validators.required]],
    });

    // Recupera o e-mail e token das etapas anteriores
    this.email = sessionStorage.getItem('resetEmail') || '';
    this.token = sessionStorage.getItem('resetToken') || '';
  }

  // valida se a confirmação bate com a nova senha
  get passwordsMatch(): boolean {
    return this.form.value.newPassword === this.form.value.confirmPassword;
  }

  onSubmit() {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    if (!this.passwordsMatch) {
      this.error = 'As senhas não coincidem.';
      return;
    }

    if (!this.email || !this.token) {
      this.error = 'Sessão expirada. Por favor, refaça o processo de redefinição.';
      return;
    }

    this.loading = true;
    this.message = '';
    this.error = '';

    const { newPassword } = this.form.value;

    this.authService.changePassword(this.email, this.token, newPassword).subscribe({
      next: (res) => {
        this.loading = false;
        this.message = res.message || 'Senha alterada com sucesso!';

        // ✅ limpa dados temporários
        sessionStorage.removeItem('resetEmail');
        sessionStorage.removeItem('resetToken');

        // 🔁 redireciona após alguns segundos
        setTimeout(() => this.router.navigate(['/login']), 2000);
      },
      error: (err) => {
        this.loading = false;
        this.error = err.error?.message || 'Erro ao alterar a senha. Tente novamente.';
      }
    });
  }
}
