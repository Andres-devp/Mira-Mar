import { Component, OnInit } from '@angular/core';
import { HotelService } from '../../../../core/models/entities';
import { HotelServiceService } from '../../../../core/services/hotel-service.service';

@Component({
    selector: 'app-hotel-service-list',
    templateUrl: './hotel-service-list.component.html',
    styleUrls: ['./hotel-service-list.component.css']
})
export class HotelServiceListComponent implements OnInit {
    servicios: HotelService[] = [];

    constructor(private hotelServiceService: HotelServiceService) {}

    ngOnInit(): void {
      this.hotelServiceService.getAll().subscribe({
        next: (data) => this.servicios = data,
        error: (err) => console.error('Error loading services:', err)
      });
    }

    onImageError(event: Event): void {
        const element = event.target as HTMLImageElement | null;

        if (!element) {
            return;
        }

        element.src = 'https://via.placeholder.com/600x400/cdc392/ffffff?text=Servicio';
    }
}