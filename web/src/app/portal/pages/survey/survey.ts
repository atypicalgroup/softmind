import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { SurveyService, SurveyModel } from '../../service/survey';

@Component({
  selector: 'app-survey',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './survey.html',
  styleUrl: './survey.scss'
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

  goToCreate(): void {
    this.router.navigate(['/portal/pesquisas/cadastrar']);
  }

  viewSurvey(survey: SurveyModel): void {
    this.router.navigate(['/portal/pesquisas/visualizar', survey.id]);
  }

  editSurvey(survey: SurveyModel): void {
    this.router.navigate(['/portal/pesquisas/editar', survey.id]);
  }

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
