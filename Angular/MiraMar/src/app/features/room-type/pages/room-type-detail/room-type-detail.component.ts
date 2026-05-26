import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { RoomType } from '../../../../core/models/entities';
import { RoomTypeService } from '../../../../core/services/room-type.service';

@Component({
  selector: 'app-room-type-detail',
  templateUrl: './room-type-detail.component.html',
  styleUrls: ['./room-type-detail.component.css'],
})
export class RoomTypeDetailComponent implements OnInit {
  tipo?: RoomType;
  nochesDemo = 3;
  checkinDate = '';
  checkoutDate = '';
  guests = 1;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private roomTypeService: RoomTypeService,
  ) {}

  ngOnInit(): void {
    this.route.paramMap.subscribe((params) => {
      const id = Number(params.get('id'));
      this.roomTypeService.getById(id).subscribe({
        next: (data) => (this.tipo = data),
        error: (err) => console.error('Error loading room type:', err),
      });
    });

    const today = new Date();
    const tomorrow = new Date(today);
    tomorrow.setDate(tomorrow.getDate() + 1);
    this.checkinDate = this.formatDateForInput(today);
    this.checkoutDate = this.formatDateForInput(tomorrow);
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
    return `$${Math.round(value).toLocaleString('es-CO')}`;
  }

  goToReservation(): void {
    if (!this.tipo) {
      return;
    }

    this.router.navigate(['/reservations/add'], {
      queryParams: {
        source: 'client',
        roomTypeId: this.tipo.id,
        fechaInicio: this.checkinDate,
        fechaFin: this.checkoutDate,
        cantidadPersonas: this.guests,
      },
    });
  }

  private formatDateForInput(date: Date): string {
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const day = String(date.getDate()).padStart(2, '0');
    return `${year}-${month}-${day}`;
  }
}
