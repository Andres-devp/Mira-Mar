import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-amenity-card',
  templateUrl: './amenity-card.component.html',
  styleUrls: ['./amenity-card.component.css'],
})
export class AmenityCardComponent {
  @Input() icon: string = '';
  @Input() title: string = '';
  @Input() description: string = '';
  @Input() delay: number = 1;
}
