import { Component, OnInit } from '@angular/core';
import { Card } from '../../../shared/card/card';
import { AuthService } from '../../../core/auth/auth-service';
import { ReportService, ReportResponse } from '../../service/report';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, Card],
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.scss'
})
export class Dashboard implements OnInit {

  nameEmployee?: string;

  // 🔹 KPIs
  participation = 0;
  engagement = 0;
  wellBeingIndex = 0;
  engagedDepartments = 0;
  criticalAlerts = 0;

  // 🔹 Dados complementares
  sectors: { sector: string; participation: number }[] = [];
  moods: { name: string; value: number }[] = [];

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

  loadReport(): void {
    this.loading = true;
    this.error = undefined;

    this.reportService.getAdminReport().subscribe({
      next: (data: ReportResponse) => {
        this.loading = false;

        // 🧠 Extração dos dados principais
        this.participation = data.surveySummary.participants.total;
        this.engagement = data.weekSummary.overallEngagement;
        this.wellBeingIndex = data.currentHealthyPercentage || 0;
        this.engagedDepartments = Object.keys(data.surveySummary.participants.totalPerSector).length;
        this.criticalAlerts = data.alerts?.length ?? 0;

        // 🧠 Mapeamento dos setores
        this.sectors = Object.entries(data.surveySummary.participants.totalPerSector).map(
          ([sector, value]) => ({
            sector,
            participation: value
          })
        );

        // 🧠 Mapeamento dos sentimentos
        this.moods = Object.entries(data.moodSummary.moods).map(
          ([key, value]) => ({
            name: key,
            value
          })
        );
      },
      error: err => {
        this.loading = false;
        this.error = `Erro ao carregar relatório (${err.status || 'desconhecido'})`;
        console.error(err);
      }
    });
  }
}
