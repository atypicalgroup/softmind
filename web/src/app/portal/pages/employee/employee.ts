import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { RouterLink } from '@angular/router';
import { EmployeeService, EmployeeModel } from '../../service/employee';

@Component({
  selector: 'app-employee',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './employee.html',
  styleUrl: './employee.scss'
})
export class Employee implements OnInit {
  employees: EmployeeModel[] = [];
  loading = true;
  error?: string;

  constructor(private employeeService: EmployeeService) {}

  ngOnInit(): void {
    this.loadEmployees();
  }

  loadEmployees(): void {
    this.loading = true;
    this.error = undefined;

    this.employeeService.getAll().subscribe({
      next: (data) => {
        this.employees = data;
        this.loading = false;
      },
      error: (err) => {
        this.loading = false;
        this.error = `Erro ao carregar funcionários (${err.status || 'desconhecido'})`;
        console.error(err);
      }
    });
  }

  editEmployee(employee: EmployeeModel) {
    console.log('Editar:', employee);
    // aqui podemos navegar para uma rota de edição
    // this.router.navigate(['/portal/funcionario/editar', employee.id]);
  }

  deleteEmployee(employee: EmployeeModel) {
    if (confirm(`Deseja realmente excluir ${employee.name}?`)) {
      this.employeeService.delete(employee.id!).subscribe({
        next: () => {
          this.employees = this.employees.filter(e => e.id !== employee.id);
        },
        error: (err) => {
          console.error(err);
          alert('Erro ao excluir funcionário.');
        }
      });
    }
  }
}
