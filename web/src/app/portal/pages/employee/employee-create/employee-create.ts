import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { EmployeeService } from '../../../service/employee';

@Component({
  selector: 'app-employee-create',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './employee-create.html',
  styleUrl: './employee-create.scss'
})
export class EmployeeCreate {
  employeeForm: FormGroup;
  loading = false;
  successMessage = '';
  errorMessage = '';

  constructor(
    private fb: FormBuilder,
    private employeeService: EmployeeService,
    private router: Router
  ) {
    this.employeeForm = this.fb.group({
      name: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(6)]],
      role: ['', Validators.required],
      permission: ['EMPLOYEE', Validators.required],
      sector: ['', Validators.required],
    });
  }

  onSubmit() {
    if (this.employeeForm.invalid) return;

    this.loading = true;
    this.successMessage = '';
    this.errorMessage = '';

    const payload = this.employeeForm.value;

    this.employeeService.create(payload).subscribe({
      next: () => {
        this.loading = false;
        this.successMessage = 'Funcionário cadastrado com sucesso!';
        setTimeout(() => this.router.navigate(['/portal/funcionarios']), 1500);
      },
      error: (err) => {
        this.loading = false;
        this.errorMessage = `Erro ao cadastrar funcionário (${err.status || 'desconhecido'})`;
        console.error(err);
      }
    });
  }

  get f() {
    return this.employeeForm.controls;
  }
}
