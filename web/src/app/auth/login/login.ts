import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../auth-service';
import { FormsModule } from "@angular/forms";


@Component({
  selector: 'app-login',
  imports: [FormsModule],
  templateUrl: './login.html',
  styleUrl: './login.scss'
})
export class Login {
  username = '';
  password = '';

  constructor(private authService: AuthService, private router: Router) {}

  onSubmit(): void {
  this.authService.login(this.username, this.password).subscribe({
    next: () => this.router.navigate(['/portal/dashboard']),
    error: (err) => {
      console.error('Erro no login:', err);
      alert(err.error?.message || 'Credenciais inválidas!');
    }
  });
}

}
