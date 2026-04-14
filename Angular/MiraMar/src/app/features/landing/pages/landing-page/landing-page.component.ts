import { Component, OnInit, AfterViewInit, ElementRef } from '@angular/core';
import { RoomType } from '../../../../core/models/entities';
import { RoomTypeService } from '../../../../core/services/room-type.service';

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
export class LandingPageComponent implements OnInit, AfterViewInit {
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

  constructor(
    private roomTypeService: RoomTypeService,
    private el: ElementRef
  ) {}

  ngOnInit(): void {
    this.roomTypeService.findAll().subscribe((tipos) => {
      this.tiposDestacados = tipos.slice(0, 3);
    });
  }

  /**
   * Scroll reveal logic — exact port of the Thymeleaf script.js IntersectionObserver.
   */
  ngAfterViewInit(): void {
    const nativeEl = this.el.nativeElement as HTMLElement;
    const revealEls = nativeEl.querySelectorAll('.reveal, .reveal-wave');

    if (!revealEls.length) return;

    if (!('IntersectionObserver' in window)) {
      revealEls.forEach((el) => el.classList.add('visible'));
      return;
    }

    const revealObserver = new IntersectionObserver(
      (entries) => {
        entries.forEach((entry) => {
          if (entry.isIntersecting) {
            entry.target.classList.add('visible');
            revealObserver.unobserve(entry.target);
          }
        });
      },
      { threshold: 0.08 }
    );

    revealEls.forEach((el) => {
      const rect = el.getBoundingClientRect();
      if (rect.top < window.innerHeight && rect.bottom > 0) {
        el.classList.add('no-anim', 'visible');
      } else {
        revealObserver.observe(el);
      }
    });
  }
}
