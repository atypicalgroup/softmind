import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { Router} from '@angular/router';

@Component({
  selector: 'app-support',
  imports: [ CommonModule],
  templateUrl: './support.html',
  styleUrl: './support.scss'
})
export class Support {
  supportPoints = [
    {
      name: 'Central de Atendimento de RH',
      description: 'Atendimento relacionado a dúvidas de folha de pagamento e benefícios',
      contactNumber: ['+55 11 99999-9999', '+55 11 98888-8888']
    },
    {
      name: 'TI - Suporte Técnico',
      description: 'Apoio em equipamentos, rede e sistemas internos',
      contactNumber: ['+55 11 97777-7777']
    }
  ];

  constructor(private router: Router) {}

  goToCreate() {
    this.router.navigate(['/portal/support-points/create']);
  }

  editPoint(point: any) {
    console.log('Editar:', point);
    // redirecionar para tela de edição
  }

  deletePoint(point: any) {
    console.log('Excluir:', point);
    // chamar service para remover
  }
}
