import { Component, OnInit } from '@angular/core';
import { RoomType } from '../../../../core/models/entities';
import { RoomTypeMockService } from '../../../../core/services/room-type-mock.service';

@Component({
  selector: 'app-room-type-list',
  templateUrl: './room-type-list.component.html',
  styleUrls: ['./room-type-list.component.css']
})
export class RoomTypeListComponent implements OnInit {
  tiposHabitacion: RoomType[] = [];
  warningMessage = '';

  constructor(private roomTypeService: RoomTypeMockService) {}

  ngOnInit(): void {
    this.tiposHabitacion = this.roomTypeService.listar();
  }

  deleteTipo(type: RoomType): void {
    const confirmed = window.confirm('¿Seguro que deseas eliminar este tipo?');

    if (!confirmed) {
      return;
    }

    const result = this.roomTypeService.eliminar(type.id);
    this.warningMessage = result.success ? '' : result.message || '';
  }
}
