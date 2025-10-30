import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { SurveyService, SurveyModel } from '../../../service/survey';

@Component({
  selector: 'app-survey-view',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './survey-view.html',
  styleUrls: ['./survey-view.scss']
})
export class SurveyView implements OnInit {
  survey?: SurveyModel;
  loading = true;
  error?: string;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private surveyService: SurveyService
  ) {}

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) this.loadSurvey(id);
  }

  loadSurvey(id: string): void {
    this.loading = true;
    this.surveyService.getById(id).subscribe({
      next: (data) => {
        this.survey = data;
        this.loading = false;
      },
      error: (err) => {
        this.loading = false;
        this.error = `Erro ao carregar pesquisa (${err.status || 'desconhecido'})`;
      }
    });
  }

  goBack(): void {
    this.router.navigate(['/portal/pesquisas']);
  }

  activateSurvey(): void {
    if (!this.survey?.id) return;
    if (confirm(`Deseja ativar a pesquisa "${this.survey.title}"?`)) {
      this.surveyService.activateSurvey(this.survey.id).subscribe({
        next: (updated) => {
          this.survey = updated;
          alert('Pesquisa ativada com sucesso!');
        },
        error: () => alert('Erro ao ativar pesquisa.')
      });
    }
  }

  deleteSurvey(): void {
    if (!this.survey?.id) return;
    if (confirm(`Tem certeza que deseja excluir a pesquisa "${this.survey.title}"?`)) {
      this.surveyService.delete(this.survey.id).subscribe({
        next: () => {
          alert('Pesquisa excluída.');
          this.goBack();
        },
        error: () => alert('Erro ao excluir pesquisa.')
      });
    }
  }
}
