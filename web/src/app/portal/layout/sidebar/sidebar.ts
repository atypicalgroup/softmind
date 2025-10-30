import { Component } from '@angular/core';
import { Router, RouterLink, RouterLinkActive } from '@angular/router';
import { NgOptimizedImage } from "@angular/common";
import { AuthService } from '../../../core/auth/auth-service';

@Component({
  selector: 'app-sidebar',
  imports: [RouterLinkActive, RouterLink, NgOptimizedImage],
  templateUrl: './sidebar.html',
  styleUrl: './sidebar.scss'
})
export class Sidebar {

  constructor(private authService: AuthService, private router: Router ){}

  onLogout():void{
    this.authService.logout();
    this.router.navigate(['login'])
  }
}
