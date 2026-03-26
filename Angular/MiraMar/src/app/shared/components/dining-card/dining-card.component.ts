import { Component, Input } from '@angular/core';

interface DiningMeta {
  icon: string;
  text: string;
}

@Component({
  selector: 'app-dining-card',
  templateUrl: './dining-card.component.html',
  styleUrls: ['./dining-card.component.css'],
})
export class DiningCardComponent {
  @Input() titulo: string = '';
  @Input() descripcion: string = '';
  @Input() metas: DiningMeta[] = [];
  @Input() botonTexto: string = '';
  @Input() botonHref: string = '#dining';
  @Input() delay: number = 1;
}
