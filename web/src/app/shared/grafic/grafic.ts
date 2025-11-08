import {
  Component,
  ElementRef,
  ViewChild,
  AfterViewInit,
  Input,
  OnChanges,
  SimpleChanges,
  OnDestroy
} from '@angular/core';
import { Chart, ChartConfiguration, registerables } from 'chart.js';

Chart.register(...registerables);

@Component({
  selector: 'app-grafic',
  standalone: true,
  templateUrl: './grafic.html',
  styleUrls: ['./grafic.scss']
})
export class Grafic implements AfterViewInit, OnChanges, OnDestroy {
  @ViewChild('chartCanvas', { static: true }) chartRef!: ElementRef<HTMLCanvasElement>;
  private chartInstance?: Chart;

  /** 🔹 Dados dinâmicos vindos do Dashboard */
  @Input() data: number[] = [];
  @Input() labels: string[] = [];
  @Input() title: string = 'Índice de Bem-Estar (%)';

  /** 🔹 Cores personalizáveis */
  @Input() lineColor = '#28a745';
  @Input() backgroundColor = 'rgba(40,167,69,0.15)';

  ngAfterViewInit(): void {
    // Cria o gráfico após o carregamento da view
    if (this.data?.length) {
      this.createChart();
    }
  }

  ngOnChanges(changes: SimpleChanges): void {
    // Atualiza se o gráfico já existir e houver mudança em dados ou labels
    if (this.chartInstance && (changes['data'] || changes['labels'])) {
      this.updateChart();
    }
  }

  ngOnDestroy(): void {
    // Destrói o gráfico ao sair do componente (evita memory leaks)
    this.destroyChart();
  }

  /** 🔧 Criação do gráfico */
  private createChart(): void {
    const ctx = this.chartRef.nativeElement.getContext('2d');
    if (!ctx) return;

    const config: ChartConfiguration<'line'> = {
      type: 'line',
      data: {
        labels: this.labels ?? [],
        datasets: [
          {
            label: this.title,
            data: this.data ?? [],
            fill: true,
            borderColor: this.lineColor,
            backgroundColor: this.backgroundColor,
            tension: 0.35,
            pointRadius: 5,
            pointBackgroundColor: this.lineColor,
            pointBorderColor: '#ffffff',
            pointHoverRadius: 7,
            borderWidth: 2
          }
        ]
      },
      options: {
        responsive: true,
        maintainAspectRatio: false,
        animation: {
          duration: 1200,
          easing: 'easeOutQuart'
        },
        scales: {
          y: {
            beginAtZero: true,
            suggestedMax: Math.max(...this.data, 100),
            title: {
              display: true,
              text: 'Índice (%)',
              color: '#ffffff',
              font: { size: 14, weight: 500 }
            },
            ticks: {
              color: '#ffffff',
              font: { size: 12 }
            },
            grid: {
              color: 'rgba(255,255,255,0.1)'
            }
          },
          x: {
            title: {
              display: true,
              text: 'Período',
              color: '#ffffff',
              font: { size: 14, weight: 500 }
            },
            ticks: {
              color: '#ffffff',
              font: { size: 12 }
            },
            grid: {
              color: 'rgba(255,255,255,0.1)'
            }
          }
        },
        plugins: {
          legend: {
            display: false
          },
          tooltip: {
            backgroundColor: '#1e1e1e',
            titleColor: '#ffffff',
            bodyColor: '#ffffff',
            padding: 10,
            cornerRadius: 6,
            displayColors: false
          }
        }
      }
    };

    this.chartInstance = new Chart(ctx, config);
  }

  /** 🔄 Atualiza o gráfico existente */
  private updateChart(): void {
    if (!this.chartInstance) return;

    this.chartInstance.data.labels = this.labels ?? [];
    this.chartInstance.data.datasets[0].data = this.data ?? [];
    this.chartInstance.options!.scales!['y']!.suggestedMax = Math.max(...this.data, 100);
    this.chartInstance.update();
  }

  /** 🧹 Limpa o gráfico para recriação */
  private destroyChart(): void {
    if (this.chartInstance) {
      this.chartInstance.destroy();
      this.chartInstance = undefined;
    }
  }
}
