import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';

@Component({
  selector: 'app-validate',
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './validate.html',
  styleUrl: './validate.scss'
})
export class Validate {
  form = this.fb.group({
    d1: ['', [Validators.required, Validators.pattern('[0-9]')]],
    d2: ['', [Validators.required, Validators.pattern('[0-9]')]],
    d3: ['', [Validators.required, Validators.pattern('[0-9]')]],
    d4: ['', [Validators.required, Validators.pattern('[0-9]')]],
  });

  constructor(private fb: FormBuilder) {}

  // Junta os dígitos em um código só
  get code(): string {
    const { d1, d2, d3, d4 } = this.form.value;
    return `${d1}${d2}${d3}${d4}`;
  }

  onSubmit() {
    if (this.form.valid) {
      console.log('Código digitado:', this.code);
      // aqui você pode chamar seu service p/ validar no backend
    } else {
      this.form.markAllAsTouched();
    }
  }
}
