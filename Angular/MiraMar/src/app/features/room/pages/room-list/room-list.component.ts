import { Component, OnInit } from '@angular/core';
import { Room } from '../../../../core/models/entities';
import { RoomService } from '../../../../core/services/room.service';

@Component({
  selector: 'app-room-list',
  templateUrl: './room-list.component.html',
  styleUrls: ['./room-list.component.css']
})
export class RoomListComponent implements OnInit {
  rooms: Room[] = [];
  warningMessage = '';

  constructor(private roomService: RoomService) {}

  ngOnInit(): void {
    this.loadRooms();
  }

  loadRooms(): void {
    this.roomService.findAll().subscribe((rooms) => {
      this.rooms = rooms;
    });
  }

  deleteRoom(room: Room): void {
    const confirmed = window.confirm(`¿Seguro que deseas eliminar la habitación ${room.nombre}?`);
    if (!confirmed) return;

    this.roomService.delete(room.id).subscribe({
      next: () => {
        this.warningMessage = '';
        this.loadRooms();
      },
      error: (err) => {
        this.warningMessage = err.error?.error || 'Error al eliminar la habitación';
      }
    });
  }
}
