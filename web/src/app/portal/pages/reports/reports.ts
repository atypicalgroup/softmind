import { Component, OnInit, ElementRef, ViewChild } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReportService, ReportResponse, SurveySummary } from '../../service/report';
import jsPDF from 'jspdf';
import html2canvas from 'html2canvas';
import { Grafic } from "../../../shared/grafic/grafic";

@Component({
  selector: 'app-reports',
  standalone: true,
  imports: [CommonModule, Grafic],
  templateUrl: './reports.html',
  styleUrls: ['./reports.scss']
})
export class Reports implements OnInit {
  @ViewChild('reportContent') reportContent!: ElementRef;

  /** 🔹 Dados principais */
  report?: ReportResponse;
  mainSurvey?: SurveySummary;

  /** 🔹 Dados para o gráfico de sentimentos */
  labelList: string[] = [];
  dataList: number[] = [];

  /** 🔹 Estado da tela */
  loading = true;
  error?: string;

  constructor(private reportService: ReportService) {}

  ngOnInit(): void {
    this.loadReport();
  }

  /** 🔹 Carrega o relatório da API */
  loadReport(): void {
    this.loading = true;
    this.error = undefined;

    this.reportService.getAdminReport().subscribe({
      next: (data) => {
        this.report = data;
        this.mainSurvey = data.surveySummary?.[0];

        // Gera os dados para o gráfico
        const moods = Object.entries(data.moodSummary?.moods ?? {}).map(([name, value]) => ({
          name,
          value: Number(value ?? 0)
        }));

        this.labelList = moods.map(m => m.name);
        this.dataList = moods.map(m => m.value);

        this.loading = false;
      },
      error: (err) => {
        this.loading = false;
        this.error = `Erro ao carregar relatório (${err.status || 'desconhecido'})`;
        console.error('Erro ao carregar relatório:', err);
      }
    });
  }

  /** 🔹 Formata percentual */
  formatPercent(value?: number): string {
    return value != null ? `${value.toFixed(1)}%` : '--';
  }

  /** 🔹 Formata data */
  formatDate(value?: string | null): string {
    return value ? new Date(value).toLocaleDateString('pt-BR') : '--';
  }

  /** 🔹 Exporta o conteúdo do relatório para PDF */
  async exportToPDF(): Promise<void> {
    if (!this.reportContent) return;

    const element = this.reportContent.nativeElement;
    const canvas = await html2canvas(element, { scale: 2 });
    const imgData = canvas.toDataURL('image/png');

    const pdf = new jsPDF('p', 'mm', 'a4');
    const imgProps = pdf.getImageProperties(imgData);
    const pdfWidth = pdf.internal.pageSize.getWidth();
    const pdfHeight = (imgProps.height * pdfWidth) / imgProps.width;

    pdf.addImage(imgData, 'PNG', 0, 0, pdfWidth, pdfHeight);
    pdf.save('Relatorio_Softmind.pdf');
  }
}
