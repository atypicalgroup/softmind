import { Component, OnInit } from '@angular/core';
import { Router, RouterLink, RouterLinkActive } from '@angular/router';
import { NgOptimizedImage } from "@angular/common";
import { AuthService } from '../../../core/auth/auth-service';

@Component({
  selector: 'app-sidebar',
  standalone: true,
  imports: [RouterLinkActive, RouterLink, NgOptimizedImage],
  templateUrl: './sidebar.html',
  styleUrls: ['./sidebar.scss']
})
export class Sidebar implements OnInit {

  /** 👤 Nome do usuário logado */
  userName: string = '';

  constructor(
    private authService: AuthService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.loadUser();
  }

  /** 🔹 Obtém o nome do usuário logado a partir do AuthService */
  private loadUser(): void {
    const user = this.authService.getUser();
    this.userName = user?.name || 'Gerenciador';
  }

  /** 🔹 Logout */
  onLogout(): void {
    this.authService.logout();
    this.router.navigate(['login']);
  }
}
