import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormArray, FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { SupportService } from '../../../service/support';

@Component({
  selector: 'app-support-create',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './support-create.html',
  styleUrl: './support-create.scss'
})
export class SupportCreate {
  form: FormGroup;
  loading = false;
  successMessage = '';
  errorMessage = '';

  constructor(
    private fb: FormBuilder,
    private router: Router,
    private supportService: SupportService
  ) {
    this.form = this.fb.group({
      name: ['', Validators.required],
      description: ['', Validators.required],
      contactNumber: this.fb.array([this.fb.control('', Validators.required)])
    });
  }

  get contacts(): FormArray {
    return this.form.get('contactNumber') as FormArray;
  }

  addContact(): void {
    this.contacts.push(this.fb.control('', Validators.required));
  }

  removeContact(index: number): void {
    this.contacts.removeAt(index);
  }

  onSubmit(): void {
    if (this.form.invalid) return;

    this.loading = true;
    const payload = this.form.value;

    this.supportService.create(payload).subscribe({
      next: () => {
        this.loading = false;
        this.successMessage = 'Ponto de apoio cadastrado com sucesso!';
        setTimeout(() => this.router.navigate(['/portal/suporte']), 1500);
      },
      error: (err) => {
        this.loading = false;
        this.errorMessage = `Erro ao salvar ponto (${err.status || 'desconhecido'})`;
        console.error(err);
      }
    });
  }
}
