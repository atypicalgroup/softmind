import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-employee',
  imports: [CommonModule, ReactiveFormsModule, RouterLink],
  templateUrl: './employee.html',
  styleUrl: './employee.scss'
})
export class Employee {
  employees = [
    {
      name: 'Maria da Silva',
      email: 'maria.silva@empresa.com.br',
      role: 'Analista de RH',
      permission: 'EMPLOYEE',
      sector: 'Recursos Humanos'
    },
    {
      name: 'João Pereira',
      email: 'joao.pereira@empresa.com.br',
      role: 'Administrador',
      permission: 'ADMIN',
      sector: 'Tecnologia'
    }
  ];

  editEmployee(employee: any) {
    console.log('Editar:', employee);
    // aqui você pode redirecionar para o formulário de edição
  }

  deleteEmployee(employee: any) {
    console.log('Excluir:', employee);
    // aqui você chama o service para remover no backend
  }
}
