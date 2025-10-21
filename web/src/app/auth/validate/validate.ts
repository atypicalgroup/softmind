import { CommonModule } from '@angular/common';
import { Component, ElementRef, QueryList, ViewChildren } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../auth-service';

@Component({
  selector: 'app-validate',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './validate.html',
  styleUrls: ['./validate.scss']
})
export class Validate {
  form!: FormGroup;
  email: string = '';
  message: string = '';
  error: string = '';
  loading: boolean = false;

  // Seleciona os 6 inputs para manipular o foco
  @ViewChildren('codeInput') inputs!: QueryList<ElementRef<HTMLInputElement>>;

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router
  ) {
    this.form = this.fb.group({
      d1: ['', [Validators.required, Validators.pattern('[0-9]')]],
      d2: ['', [Validators.required, Validators.pattern('[0-9]')]],
      d3: ['', [Validators.required, Validators.pattern('[0-9]')]],
      d4: ['', [Validators.required, Validators.pattern('[0-9]')]],
      d5: ['', [Validators.required, Validators.pattern('[0-9]')]],
      d6: ['', [Validators.required, Validators.pattern('[0-9]')]],
    });

    this.email = sessionStorage.getItem('resetEmail') || '';
  }

  get code(): string {
    const { d1, d2, d3, d4, d5, d6 } = this.form.value;
    return `${d1}${d2}${d3}${d4}${d5}${d6}`;
  }

  onInput(event: any, index: number) {
    const input = event.target as HTMLInputElement;
    const value = input.value.replace(/[^0-9]/g, '');
    input.value = value;

    // Avança para o próximo campo se houver dígito
    if (value && index < 5) {
      this.inputs.get(index + 1)?.nativeElement.focus();
    }

    // Retrocede se apagar o dígito
    if (!value && index > 0) {
      this.inputs.get(index - 1)?.nativeElement.focus();
    }
  }

  onSubmit() {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    if (!this.email) {
      this.error = 'E-mail não encontrado. Volte para a etapa anterior.';
      return;
    }

    this.loading = true;
    this.message = '';
    this.error = '';

    this.authService.verifyToken(this.email, this.code).subscribe({
      next: (res) => {
        this.loading = false;
        this.message = res.message || 'Código válido!';

        // Armazena token temporariamente
        sessionStorage.setItem('resetToken', this.code);

        // Mensagem temporária de sucesso
        setTimeout(() => {
          this.message = '';
          this.router.navigate(['/redefinir-senha']);
        }, 2000);
      },
      error: (err) => {
        this.loading = false;
        this.error = err.error?.message || 'Código inválido ou expirado.';

        // Limpa erro após 3 segundos
        setTimeout(() => (this.error = ''), 3000);
      }
    });
  }
}
