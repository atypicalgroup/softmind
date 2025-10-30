import { Component, OnInit, ElementRef, ViewChild } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReportService, ReportResponse } from '../../service/report';
import jsPDF from 'jspdf';
import html2canvas from 'html2canvas';

@Component({
  selector: 'app-reports',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './reports.html',
  styleUrl: './reports.scss'
})
export class Reports implements OnInit {

  @ViewChild('reportContent') reportContent!: ElementRef;

  report?: ReportResponse;
  loading = true;
  error?: string;

  constructor(private reportService: ReportService) {}

  ngOnInit(): void {
    this.loadReport();
  }

  loadReport(): void {
    this.loading = true;
    this.error = undefined;

    this.reportService.getAdminReport().subscribe({
      next: (data) => {
        this.report = data;
        this.loading = false;
      },
      error: (err) => {
        this.loading = false;
        this.error = `Erro ao carregar relatório (${err.status || 'desconhecido'})`;
        console.error(err);
      }
    });
  }

  formatPercent(value?: number): string {
    return value != null ? `${value.toFixed(1)}%` : '--';
  }

  formatDate(value?: string): string {
    return value ? new Date(value).toLocaleDateString('pt-BR') : '--';
  }

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
