import { Component, OnInit } from '@angular/core';
import { HotelService } from '../../../../core/models/entities';
import { HotelServiceMockService } from '../../../../core/services/hotel-service-mock.service';

@Component({
    selector: 'app-hotel-service-list',
    templateUrl: './hotel-service-list.component.html',
    styleUrls: ['./hotel-service-list.component.css']
})
export class HotelServiceListComponent implements OnInit {
    servicios: HotelService[] = [];
    warningMessage = '';

constructor(private hotelServiceService: HotelServiceMockService) {}

ngOnInit(): void {
    this.servicios = this.hotelServiceService.listar();
}

deleteServicio(service: HotelService): void {
    const confirmed = window.confirm('¿Seguro que deseas eliminar este servicio?');

    if (!confirmed) {
    return;
    }

    const result = this.hotelServiceService.eliminar(service.id);
    this.warningMessage = result.success ? '' : result.message || '';
    
    if (result.success) {
      this.servicios = this.hotelServiceService.listar();
    }
}
}