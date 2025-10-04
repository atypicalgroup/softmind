import { Component } from '@angular/core';
import { Router, RouterLink } from '@angular/router';

@Component({
  selector: 'app-survey',
  imports: [RouterLink],
  templateUrl: './survey.html',
  styleUrl: './survey.scss'
})
export class Survey {
  surveys = [
    {
      title: 'Pesquisa de Clima Organizacional',
      description: 'Pesquisa sobre satisfação dos colaboradores',
      questions: [
        { text: 'Como você avalia sua carga de trabalho?', type: 'SINGLE_CHOICE', options: ['Leve', 'Média', 'Alta'] },
        { text: 'Você trabalha além do seu horário regular?', type: 'SINGLE_CHOICE', options: ['Nunca', 'Às vezes', 'Sempre'] }
      ]
    },
    {
      title: 'Pesquisa de Benefícios',
      description: 'Feedback sobre benefícios oferecidos pela empresa',
      questions: [
        { text: 'O plano de saúde atende suas necessidades?', type: 'SINGLE_CHOICE', options: ['Sim', 'Não'] }
      ]
    }
  ];

  constructor(private router: Router) {}

  goToCreate() {
    this.router.navigate(['/portal/surveys/create']);
  }

  viewSurvey(survey: any) {
    console.log('Visualizar:', survey);
    // pode abrir um modal com detalhes da pesquisa
  }

  editSurvey(survey: any) {
    console.log('Editar:', survey);
    // pode redirecionar para a tela de edição
  }

  deleteSurvey(survey: any) {
    console.log('Excluir:', survey);
    // chamar service para excluir no backend
  }
}
