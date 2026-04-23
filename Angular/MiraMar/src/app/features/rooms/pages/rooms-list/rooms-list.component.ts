import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { RoomType } from '../../../../core/models/entities';
import { RoomTypeService } from '../../../../core/services/room-type.service';

@Component({
  selector: 'app-rooms-list',
  templateUrl: './rooms-list.component.html',
  styleUrls: ['./rooms-list.component.css']
})
export class RoomsListComponent implements OnInit {
  tipos: RoomType[] = [];
  
  capacidad: number | '' = '';
  precioMax: number | '' = '';
  fechaInicio: string = '';
  fechaFin: string = '';

  constructor(
    private roomTypeSvc: RoomTypeService,
    private route: ActivatedRoute,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.route.queryParams.subscribe(params => {
      if (params['capacidad']) this.capacidad = +params['capacidad'];
      if (params['precioMax']) this.precioMax = +params['precioMax'];
      if (params['fechaInicio']) this.fechaInicio = params['fechaInicio'];
      if (params['fechaFin']) this.fechaFin = params['fechaFin'];
      
      this.loadRooms();
    });
  }
  
  loadRooms(): void {
    const cap = this.capacidad !== '' ? this.capacidad : undefined;
    const max = this.precioMax !== '' ? this.precioMax : undefined;
    const start = this.fechaInicio ? this.fechaInicio : undefined;
    const end = this.fechaFin ? this.fechaFin : undefined;
    
    this.roomTypeSvc.filter(cap, max, start, end).subscribe({
      next: (data) => this.tipos = data,
      error: (err) => console.error('Error loading room types:', err)
    });
  }

  filterRooms(): void {
    this.router.navigate([], {
      relativeTo: this.route,
      queryParams: {
        capacidad: this.capacidad || null,
        precioMax: this.precioMax || null,
        fechaInicio: this.fechaInicio || null,
        fechaFin: this.fechaFin || null
      },
      queryParamsHandling: 'merge'
    });
  }

  clearFilters(): void {
    this.capacidad = '';
    this.precioMax = '';
    this.fechaInicio = '';
    this.fechaFin = '';
    
    this.router.navigate([], {
      relativeTo: this.route,
      queryParams: {
        capacidad: null,
        precioMax: null,
        fechaInicio: null,
        fechaFin: null
      },
      queryParamsHandling: 'merge'
    });
  }
}
