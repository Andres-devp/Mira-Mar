import { Component, OnInit } from '@angular/core';
import { HotelService } from '../../../../core/models/entities';
import { HotelServiceService } from '../../../../core/services/hotel-service.service';

@Component({
  selector: 'app-hotel-service-table',
  templateUrl: './hotel-service-table.component.html',
  styleUrls: ['./hotel-service-table.component.css']
})
export class HotelServiceTableComponent implements OnInit {
  servicios: HotelService[] = [];
  warningMessage = '';

  constructor(private hotelServiceService: HotelServiceService) {}

  ngOnInit(): void {
    this.loadServicios();
  }

  loadServicios(): void {
    this.hotelServiceService.getAll().subscribe({
      next: (data) => this.servicios = data,
      error: (err) => console.error('Error loading services:', err)
    });
  }

  deleteServicio(service: HotelService): void {
    const confirmed = window.confirm('¿Seguro que deseas eliminar este servicio?');

    if (!confirmed) {
      return;
    }

    this.hotelServiceService.delete(service.id).subscribe({
      next: () => {
        this.warningMessage = '';
        this.loadServicios();
      },
      error: (err) => {
        this.warningMessage = err.error?.error || 'Error al eliminar el servicio. Puede que tenga consumos asociados.';
      }
    });
  }
}
