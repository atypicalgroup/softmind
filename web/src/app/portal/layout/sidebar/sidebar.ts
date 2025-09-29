import { Component } from '@angular/core';
import { RouterLink, RouterLinkActive } from '@angular/router';
import { NgOptimizedImage } from "@angular/common";

@Component({
  selector: 'app-sidebar',
  imports: [RouterLinkActive, RouterLink, NgOptimizedImage],
  templateUrl: './sidebar.html',
  styleUrl: './sidebar.scss'
})
export class Sidebar {

}
