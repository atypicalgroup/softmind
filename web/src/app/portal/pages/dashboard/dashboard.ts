import { Component } from '@angular/core';
import { Card } from '../../../shared/card/card';
import { AuthService } from '../../../auth/auth-service';

@Component({
  selector: 'app-dashboard',
  imports: [Card],
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.scss'
})
export class Dashboard {

  nameEmployee: string | undefined;

  constructor(private authService: AuthService){
    this.nameEmployee = this.authService.getUser()?.name;
  }
}
