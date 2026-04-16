import { Component, OnInit } from '@angular/core';
import { RoomType } from '../../../../core/models/entities';
import { RoomTypeService } from '../../../../core/services/room-type.service';

@Component({
  selector: 'app-rooms-list',
  templateUrl: './rooms-list.component.html',
  styleUrls: ['./rooms-list.component.css']
})
export class RoomsListComponent implements OnInit {
  tipos: RoomType[] = [];

  constructor(private roomTypeSvc: RoomTypeService) {}

  ngOnInit(): void {
    this.roomTypeSvc.getAll().subscribe({
      next: (data) => this.tipos = data,
      error: (err) => console.error('Error loading room types:', err)
    });
  }
}
