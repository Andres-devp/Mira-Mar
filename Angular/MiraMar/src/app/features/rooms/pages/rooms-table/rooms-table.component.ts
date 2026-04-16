import { Component, OnInit } from '@angular/core';
import { Room } from '../../../../core/models/entities';
import { RoomService } from '../../../../core/services/room.service';

@Component({
  selector: 'app-rooms-table',
  templateUrl: './rooms-table.component.html',
  styleUrls: ['./rooms-table.component.css']
})
export class RoomsTableComponent implements OnInit {
  rooms: Room[] = [];
  warningMessage = '';

  constructor(private roomService: RoomService) {}

  ngOnInit(): void {
    this.loadRooms();
  }

  loadRooms(): void {
    this.roomService.getAll().subscribe({
      next: (data) => this.rooms = data,
      error: (err) => console.error('Error loading rooms:', err)
    });
  }

  deleteRoom(room: Room): void {
    if (confirm('¿Confirma que desea eliminar la habitación "' + room.nombre + '"?')) {
      this.roomService.delete(room.id).subscribe({
        next: () => {
          this.warningMessage = '';
          this.loadRooms();
        },
        error: (err) => {
          this.warningMessage = err.error?.error || 'Error al eliminar la habitación. Puede que tenga reservas asociadas.';
        }
      });
    }
  }
}
