import { Component, OnInit } from '@angular/core';
import { RoomType } from '../../../../core/models/entities';
import { RoomTypeMockService } from '../../../../core/services/room-type-mock.service';

interface Amenity {
  icon: string;
  title: string;
  description: string;
  delay: number;
}

interface DiningMeta {
  icon: string;
  text: string;
}

interface Dining {
  titulo: string;
  descripcion: string;
  metas: DiningMeta[];
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
  tiposDestacados: RoomType[] = [];
  amenities: Amenity[] = [
    {
      icon: 'ph-waves',
      title: 'Playa Privada',
      description:
        'Arenas blancas prístinas con servicio de toallas exclusivo.',
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
      description:
        'Tratamientos con ingredientes locales como sal marina y aloe.',
      delay: 3,
    },
    {
      icon: 'ph-sailboat',
      title: 'Navegación',
      description: 'Charters privados al atardecer en nuestro velero clásico.',
      delay: 4,
    },
  ];
  dinings: Dining[] = [
    {
      titulo: 'Ocean Table',
      descripcion:
        'Cocina fresca con ingredientes locales, mariscos y opciones saludables.',
      metas: [
        { icon: 'ph-clock', text: '7:00 - 22:00' },
        { icon: 'ph-leaf', text: 'Opciones healthy' },
      ],
      botonTexto: 'Ver menú',
      botonHref: '#dining',
      delay: 1,
    },
    {
      titulo: 'Pool Bar',
      descripcion:
        'Cocteles tropicales, jugos naturales y snacks para tardes de sol.',
      metas: [
        { icon: 'ph-sun', text: 'Todo el día' },
        { icon: 'ph-cocktail', text: 'Signature drinks' },
      ],
      botonTexto: 'Especiales',
      botonHref: '#dining',
      delay: 2,
    },
    {
      titulo: 'Sunset Lounge',
      descripcion: 'Tapas caribeñas y música suave mientras cae el atardecer.',
      metas: [
        { icon: 'ph-music-notes', text: 'Live sessions' },
        { icon: 'ph-sun-horizon', text: 'Atardecer' },
      ],
      botonTexto: 'Reservar',
      botonHref: '#dining',
      delay: 3,
    },
  ];

  constructor(private roomTypeService: RoomTypeMockService) {}

  ngOnInit(): void {
    this.tiposDestacados = this.roomTypeService.listarDestacados(3);
  }
}
