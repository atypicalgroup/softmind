import { Component } from '@angular/core';
import { FormBuilder, FormGroup,  ReactiveFormsModule,  Validators } from '@angular/forms';

@Component({
  selector: 'app-employee-create',
  imports: [ReactiveFormsModule],
  templateUrl: './employee-create.html',
  styleUrl: './employee-create.scss'
})
export class EmployeeCreate {
  employeeForm: FormGroup;

  constructor(private fb: FormBuilder) {
    this.employeeForm = this.fb.group({
      name: ['Maria da Silva', Validators.required],
      email: ['maria.silva@empresa.com.br', [Validators.required, Validators.email]],
      password: ['User@123', [Validators.required, Validators.minLength(6)]],
      role: ['Analista de RH', Validators.required],
      permission: ['EMPLOYEE', Validators.required],
      sector: ['Recursos Humanos', Validators.required],
    });
  }

  onSubmit() {
    if (this.employeeForm.valid) {
      console.log('Novo Funcionário:', this.employeeForm.value);
      // aqui você pode chamar o service do backend
      // this.employeeService.create(this.employeeForm.value).subscribe(...)
    }
  }
}
