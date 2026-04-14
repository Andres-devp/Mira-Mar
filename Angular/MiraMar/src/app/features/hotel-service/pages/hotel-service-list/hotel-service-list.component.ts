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
    warningMessage = '';

    constructor(private hotelServiceService: HotelServiceService) {}

    ngOnInit(): void {
        this.loadServicios();
    }

    loadServicios(): void {
        this.hotelServiceService.findAll().subscribe((servicios) => {
            this.servicios = servicios;
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
                this.warningMessage = err.error?.error || 'Error al eliminar el servicio';
            }
        });
    }
}