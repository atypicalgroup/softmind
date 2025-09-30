import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../auth-service';


@Component({
  selector: 'app-login',
  imports: [],
  templateUrl: './login.html',
  styleUrl: './login.scss'
})
export class Login {
  email = '';
  password = '';

  constructor(private authService: AuthService, private router: Router) {}

  onSubmit(): void {
    this.authService.login(this.email, this.password).subscribe({
      next: () => this.router.navigate(['/dashboard']),
      error: () => alert('Credenciais inválidas!')
    });
  }
}
