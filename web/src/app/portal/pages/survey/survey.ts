import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { SurveyService, SurveyModel } from '../../service/survey';

@Component({
  selector: 'app-survey',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './survey.html',
  styleUrls: ['./survey.scss']
})
export class Survey implements OnInit {
  surveys: SurveyModel[] = [];
  loading = true;
  error?: string;

  constructor(
    private router: Router,
    private surveyService: SurveyService
  ) {}

  ngOnInit(): void {
    this.loadSurveys();
  }

  /** 🔹 Carrega todas as pesquisas da empresa logada */
  loadSurveys(): void {
    this.loading = true;
    this.error = undefined;

    this.surveyService.getAll().subscribe({
      next: (data) => {
        this.surveys = data;
        this.loading = false;
      },
      error: (err) => {
        this.loading = false;
        this.error = `Erro ao carregar pesquisas (${err.status || 'desconhecido'})`;
        console.error(err);
      }
    });
  }

  /** 🔹 Navega para criação de nova pesquisa */
  goToCreate(): void {
    this.router.navigate(['/portal/pesquisa/cadastrar']);
  }

  /** 🔹 Visualiza detalhes da pesquisa */
  viewSurvey(survey: SurveyModel): void {
    this.router.navigate(['/portal/pesquisa/visualizar', survey.id]);
  }

  /** 🔹 Edita uma pesquisa existente */
  editSurvey(survey: SurveyModel): void {
    this.router.navigate(['/portal/pesquisa/editar', survey.id]);
  }

  /** 🔹 Ativa uma pesquisa */
  activateSurvey(survey: SurveyModel): void {
    if (confirm(`Deseja ativar a pesquisa "${survey.title}"?`)) {
      this.surveyService.activateSurvey(survey.id!).subscribe({
        next: (updated) => {
          this.surveys = this.surveys.map(s =>
            s.id === updated.id ? { ...s, active: true } : { ...s, active: false }
          );
          alert('Pesquisa ativada com sucesso!');
        },
        error: (err) => {
          console.error(err);
          alert('Erro ao ativar pesquisa.');
        }
      });
    }
  }

  /** 🔹 Exclui uma pesquisa */
  deleteSurvey(survey: SurveyModel): void {
    if (confirm(`Deseja excluir a pesquisa "${survey.title}"?`)) {
      this.surveyService.delete(survey.id!).subscribe({
        next: () => {
          this.surveys = this.surveys.filter(s => s.id !== survey.id);
        },
        error: (err) => {
          console.error(err);
          alert('Erro ao excluir pesquisa.');
        }
      });
    }
  }
}
