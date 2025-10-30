import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { SupportService, SupportPoint } from '../../service/support';

@Component({
  selector: 'app-support',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './support.html',
  styleUrl: './support.scss'
})
export class Support implements OnInit {
  supportPoints: SupportPoint[] = [];
  loading = true;
  error?: string;

  constructor(
    private router: Router,
    private supportService: SupportService
  ) {}

  ngOnInit(): void {
    this.loadSupportPoints();
  }

  loadSupportPoints(): void {
    this.loading = true;
    this.error = undefined;

    this.supportService.getAll().subscribe({
      next: (data) => {
        this.supportPoints = data;
        this.loading = false;
      },
      error: (err) => {
        this.loading = false;
        this.error = `Erro ao carregar pontos de apoio (${err.status || 'desconhecido'})`;
        console.error(err);
      }
    });
  }

  goToCreate(): void {
    this.router.navigate(['/portal/suporte/cadastrar']);
  }

  editPoint(point: SupportPoint): void {
    this.router.navigate(['/portal/suporte/editar', point.id]);
  }

  deletePoint(point: SupportPoint): void {
    if (confirm(`Deseja excluir o ponto "${point.name}"?`)) {
      this.supportService.delete(point.id!).subscribe({
        next: () => {
          this.supportPoints = this.supportPoints.filter(p => p.id !== point.id);
        },
        error: (err) => {
          console.error(err);
          alert('Erro ao excluir ponto de apoio.');
        }
      });
    }
  }
}
