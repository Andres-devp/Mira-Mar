import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { RoomType } from '../../../../core/models/entities';
import { RoomTypeMockService } from '../../../../core/services/room-type-mock.service';

@Component({
  selector: 'app-room-type-detail',
  templateUrl: './room-type-detail.component.html',
  styleUrls: ['./room-type-detail.component.css']
})
export class RoomTypeDetailComponent implements OnInit {
  tipo?: RoomType;
  nochesDemo = 3;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private roomTypeService: RoomTypeMockService
  ) {}

  ngOnInit(): void {
    this.route.paramMap.subscribe((params) => {
      const id = Number(params.get('id'));
      this.tipo = this.roomTypeService.buscarPorId(id);
    });
  }

  editTipo(): void {
    if (!this.tipo) {
      return;
    }

    this.router.navigate(['/roomtypes', this.tipo.id, 'edit']);
  }

  get guestOptions(): number[] {
    if (!this.tipo) {
      return [1];
    }

    return Array.from({ length: this.tipo.capacidad }, (_, index) => index + 1);
  }

  formatPrice(value: number): string {
    return `$${value.toFixed(1)}`;
  }
}
