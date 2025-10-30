import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AuthService } from '../../../core/auth/auth-service';

@Component({
  selector: 'app-profile',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './profile.html',
  styleUrls: ['./profile.scss']
})
export class Profile implements OnInit {

  /** 🔹 Dados do usuário logado */
  user = {
    name: '',
    email: '',
    role: '',
    phone: ''
  };

  /** 🔹 Campos para alterar senha */
  password = '';
  confirmPassword = '';

  /** 🔹 Feedbacks visuais */
  successMessage?: string;
  errorMessage?: string;

  constructor(private authService: AuthService) {}

  ngOnInit(): void {
    this.loadUserProfile();
  }

  /** 🔹 Carrega os dados do usuário logado */
  private loadUserProfile(): void {
    const currentUser = this.authService.getUser();
    if (currentUser) {
      this.user = {
        name: currentUser.name || '',
        email: currentUser.email || '',
        role: currentUser.role || 'Administrador',
        phone: currentUser.phone || ''
      };
    }
  }

  /** 🔹 Salva alterações de perfil */
  onSaveProfile(): void {
    this.successMessage = '';
    this.errorMessage = '';

    // Exemplo: envia pro backend
    console.log('Salvando alterações de perfil...', this.user);

    // Sucesso simulado
    this.successMessage = 'Informações atualizadas com sucesso!';
  }

  /** 🔹 Atualiza senha */
  onChangePassword(): void {
    this.successMessage = '';
    this.errorMessage = '';

    if (this.password.trim() === '' || this.confirmPassword.trim() === '') {
      this.errorMessage = 'Preencha todos os campos de senha.';
      return;
    }

    if (this.password !== this.confirmPassword) {
      this.errorMessage = 'As senhas não coincidem.';
      return;
    }

    // Exemplo: envio da nova senha
    console.log('Alterando senha para:', this.password);

    // Sucesso simulado
    this.successMessage = 'Senha alterada com sucesso!';
    this.password = '';
    this.confirmPassword = '';
  }
}
