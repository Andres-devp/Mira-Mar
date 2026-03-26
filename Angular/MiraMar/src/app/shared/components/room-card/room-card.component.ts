import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-room-card',
  templateUrl: './room-card.component.html',
  styleUrls: ['./room-card.component.css'],
})
export class RoomCardComponent {
  @Input() id: number = 0;
  @Input() nombre: string = '';
  @Input() precioNoche: number = 0;
  @Input() urlImagen: string = '';
}
