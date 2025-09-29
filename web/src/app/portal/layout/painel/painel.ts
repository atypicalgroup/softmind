import { Component } from '@angular/core';
import { Sidebar } from '../sidebar/sidebar';
import { RouterOutlet } from '@angular/router';

@Component({
  selector: 'app-painel',
  imports: [Sidebar, RouterOutlet ],
  templateUrl: './painel.html',
  styleUrl: './painel.scss'
})
export class Painel {

}
