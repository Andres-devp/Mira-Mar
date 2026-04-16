import { Component, OnInit } from '@angular/core';
import { RoomType } from '../../../../core/models/entities';
import { RoomTypeService } from '../../../../core/services/room-type.service';

@Component({
  selector: 'app-room-type-list',
  templateUrl: './room-type-list.component.html',
  styleUrls: ['./room-type-list.component.css']
})
export class RoomTypeListComponent implements OnInit {
  tiposHabitacion: RoomType[] = [];
  warningMessage = '';

  constructor(private roomTypeService: RoomTypeService) {}

  ngOnInit(): void {
    this.loadTipos();
  }

  loadTipos(): void {
    this.roomTypeService.getAll().subscribe({
      next: (data) => this.tiposHabitacion = data,
      error: (err) => console.error('Error loading room types:', err)
    });
  }

  deleteTipo(type: RoomType): void {
    const confirmed = window.confirm('¿Seguro que deseas eliminar este tipo?');

    if (!confirmed) {
      return;
    }

    this.roomTypeService.delete(type.id).subscribe({
      next: () => {
        this.warningMessage = '';
        this.loadTipos();
      },
      error: (err) => {
        this.warningMessage = err.error?.error || 'Error al eliminar el tipo de habitación.';
      }
    });
  }
}
