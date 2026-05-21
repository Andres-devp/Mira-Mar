import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { RoomType, HotelService } from '../../../../core/models/entities';
import { RoomTypeService } from '../../../../core/services/room-type.service';
import { HotelServiceService } from '../../../../core/services/hotel-service.service';
import { AuthService } from '../../../../core/services/auth.service';

interface Amenity {
  icon: string;
  title: string;
  description: string;
  delay: number;
}

interface ServicioMeta {
  icon: string;
  text: string;
}

interface ServicioCard {
  titulo: string;
  descripcion: string;
  metas: ServicioMeta[];
  botonTexto: string;
  botonHref: string;
  delay: number;
}

@Component({
  selector: 'app-landing-page',
  templateUrl: './landing-page.component.html',
  styleUrls: ['./landing-page.component.css'],
})
export class LandingPageComponent implements OnInit {
  showMap = false;
  tiposDestacados: RoomType[] = [];

  fechaInicio: string = '';
  fechaFin: string = '';
  capacidad: string = '';

  amenities: Amenity[] = [
    {
      icon: 'ph-waves',
      title: 'Playa Privada',
      description: 'Arenas blancas prístinas con servicio de toallas exclusivo.',
      delay: 1,
    },
    {
      icon: 'ph-martini',
      title: 'Blue Lounge',
      description: 'Coctelería de autor inspirada en los colores del océano.',
      delay: 2,
    },
    {
      icon: 'ph-sparkle',
      title: 'Spa & Wellness',
      description: 'Tratamientos con ingredientes locales como sal marina y aloe.',
      delay: 3,
    },
    {
      icon: 'ph-sailboat',
      title: 'Navegación',
      description: 'Charters privados al atardecer en nuestro velero clásico.',
      delay: 4,
    },
  ];

  serviciosDestacados: ServicioCard[] = [];

  constructor(
    private roomTypeService: RoomTypeService,
    private hotelServiceService: HotelServiceService,
    private router: Router,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    if (this.authService.isLoggedIn()) {
      const role = this.authService.getRole();
      if (role === 'ADMIN') { this.router.navigate(['/admin']); return; }
      if (role === 'OPERATOR') { this.router.navigate(['/operator']); return; }
      if (role === 'CLIENT') { this.router.navigate(['/usuarios/me']); return; }
    }

    this.roomTypeService.getAll().subscribe({
      next: (tipos) => this.tiposDestacados = tipos.slice(0, 3),
      error: (err) => console.error('Error loading room types:', err)
    });

    this.hotelServiceService.getAll().subscribe({
      next: (servicios) => {
        this.serviciosDestacados = servicios.slice(0, 3).map((servicio, index) => ({
          titulo: servicio.nombre,
          descripcion: servicio.descripcion || 'Descubre esta experiencia exclusiva en Mira Mar.',
          metas: servicio.price
            ? [{ icon: 'ph-currency-dollar', text: `Desde $${servicio.price}` }]
            : [{ icon: 'ph-star', text: 'Servicio exclusivo' }],
          botonTexto: 'Ver detalle',
          botonHref: '/services',
          delay: index + 1
        }));
      },
      error: (err) => console.error('Error loading hotel services:', err)
    });
  }

  searchSuites(): void {
    const queryParams: any = {};
    if (this.fechaInicio) queryParams.fechaInicio = this.fechaInicio;
    if (this.fechaFin) queryParams.fechaFin = this.fechaFin;
    if (this.capacidad) queryParams.capacidad = this.capacidad;

    this.router.navigate(['/rooms'], {
      fragment: 'suites',
      queryParams: queryParams
    });
  }
}