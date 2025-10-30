import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Card } from '../../../shared/card/card';
import { Grafic } from '../../../shared/grafic/grafic';
import { AuthService } from '../../../core/auth/auth-service';
import { ReportService, ReportResponse } from '../../service/report';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, Card, Grafic],
  templateUrl: './dashboard.html',
  styleUrls: ['./dashboard.scss']
})
export class Dashboard implements OnInit {

  /** 👤 Nome do colaborador logado */
  nameEmployee?: string;

  /** 📊 Indicadores principais */
  participation = 0;
  engagement = 0;
  wellBeingIndex = 0;
  engagedDepartments = 0;
  criticalAlerts = 0;

  /** 📈 Dados complementares */
  sectors: { sector: string; participation: number }[] = [];
  moods: { name: string; value: number }[] = [];
  latestQuestions: { question: string; response: string; date: string }[] = [];

  /** 🎯 Dados para o gráfico */
  labelList: string[] = [];
  dataList: number[] = [];

  /** ⚙️ Estado da tela */
  loading = true;
  error?: string;

  constructor(
    private authService: AuthService,
    private reportService: ReportService
  ) {
    this.nameEmployee = this.authService.getUser()?.name;
  }

  ngOnInit(): void {
    this.loadReport();
  }

  /**
   * 🔹 Carrega o relatório administrativo
   */
  loadReport(): void {
    this.loading = true;
    this.error = undefined;

    this.reportService.getAdminReport().subscribe({
      next: (data: ReportResponse) => {
        this.populateDashboard(data);
        this.loading = false;
      },
      error: err => {
        this.loading = false;
        this.error = `Erro ao carregar relatório (${err.status || 'desconhecido'})`;
        console.error('Erro ao carregar relatório:', err);
      }
    });
  }

  /**
   * 🔹 Popula o dashboard com os dados do relatório.
   */
  private populateDashboard(data: ReportResponse): void {
    const firstSurvey = data.surveySummary?.[0];

    /** KPIs principais */
    this.participation = firstSurvey?.participants?.total ?? 0;
    this.engagement = data.weekSummary?.overallEngagement ?? 0;
    this.wellBeingIndex = data.currentHealthyPercentage ?? 0;
    this.engagedDepartments = Object.keys(firstSurvey?.participants?.totalPerSector ?? {}).length;
    this.criticalAlerts = data.alerts?.length ?? 0;

    /** Setores participantes */
    this.sectors = Object.entries(firstSurvey?.participants?.totalPerSector ?? {}).map(
      ([sector, value]) => ({
        sector,
        participation: Number(value ?? 0)
      })
    );

    /** Sentimentos (moods) */
    this.moods = Object.entries(data.moodSummary?.moods ?? {}).map(
      ([name, value]) => ({
        name,
        value: Number(value ?? 0)
      })
    );

    /** Alimenta o gráfico com arrays simples (evita erro de template) */
    this.labelList = this.moods.map(m => m.name);
    this.dataList = this.moods.map(m => m.value);

    /** Últimas respostas */
    this.latestQuestions = (firstSurvey?.questionResponses ?? [])
      .flatMap(q =>
        q.dailyResponses.map(dr => ({
          question: q.question,
          date: dr.date,
          response: dr.ranking?.[0]?.response ?? '-'
        }))
      )
      .sort((a, b) => (a.date < b.date ? 1 : -1))
      .slice(0, 5);
  }
}
