import { CommonModule } from '@angular/common';
import { Component, Input, } from '@angular/core';

@Component({
  selector: 'app-card',
  imports: [CommonModule],
  templateUrl: './card.html',
  styleUrl: './card.scss'
})
export class Card {
  @Input() title!: string;
  @Input() description!: string;
  @Input() value!: string | number;
  @Input() subtitle?: string;
  @Input() variable?: string;
  @Input() color?: 'green'| 'red' | 'blue' |  'gray' | string;

  get isPositive(): boolean {
  return this.variable?.includes('↑') ?? false;
  }

  get isNegative(): boolean {
    return this.variable?.includes('↓') ?? false;
  }
}


