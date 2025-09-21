import { Component } from '@angular/core';
import { Header } from '../header/header';
import { Sidebar } from '../sidebar/sidebar';
import { RouterOutlet } from '@angular/router';

@Component({
  selector: 'app-painel',
  imports: [Header, Sidebar, RouterOutlet ],
  templateUrl: './painel.html',
  styleUrl: './painel.scss'
})
export class Painel {

}
