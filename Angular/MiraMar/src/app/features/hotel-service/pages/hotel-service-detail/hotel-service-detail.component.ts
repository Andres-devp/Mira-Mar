import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { HotelService } from '../../../../core/models/entities';
import { HotelServiceService } from '../../../../core/services/hotel-service.service';

@Component({
    selector: 'app-hotel-service-detail',
    templateUrl: './hotel-service-detail.component.html',
    styleUrls: ['./hotel-service-detail.component.css']
})
export class HotelServiceDetailComponent implements OnInit {
    servicio?: HotelService;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private hotelServiceService: HotelServiceService
  ) {}

  ngOnInit(): void {
    this.route.paramMap.subscribe((params) => {
      const id = Number(params.get('id'));
      this.hotelServiceService.getById(id).subscribe({
        next: (data) => this.servicio = data,
        error: (err) => console.error('Error loading service:', err)
      });
    });
  }

  editServicio(): void {
    if (!this.servicio) {
      return;
    }

    this.router.navigate(['/services', this.servicio.id, 'edit']);
  }

  formatPrice(value: number): string {
    return `$${value.toFixed(1)}`;
  }
}