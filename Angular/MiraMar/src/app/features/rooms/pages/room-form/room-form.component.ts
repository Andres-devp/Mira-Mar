import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { RoomType, Room } from '../../../../core/models/entities';
import { RoomTypeService } from '../../../../core/services/room-type.service';
import { RoomService } from '../../../../core/services/room.service';

@Component({
  selector: 'app-room-form',
  templateUrl: './room-form.component.html',
  styleUrls: ['./room-form.component.css']
})
export class RoomFormComponent implements OnInit {
  roomForm!: FormGroup;
  roomTypes: RoomType[] = [];
  selectedType?: RoomType;
  isNew = true;
  pageTitle = 'Nueva Habitación';
  submitLabel = 'Crear habitación';

  constructor(
    private fb: FormBuilder,
    private rtSvc: RoomTypeService,
    private roomSvc: RoomService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.rtSvc.getAll().subscribe({
      next: (data) => this.roomTypes = data,
      error: (err) => console.error('Error loading room types:', err)
    });
    this.roomForm = this.fb.group({
      nombre: ['', Validators.required],
      tipoHabitacionId: ['', Validators.required]
    });
  }

  onTipoChange(): void {
    const selectedId = Number(this.roomForm.value.tipoHabitacionId);
    this.selectedType = this.roomTypes.find(t => t.id === selectedId);
  }

  save(): void {
    if (this.roomForm.valid) {
      const raw = this.roomForm.value;
      const selectedType = this.roomTypes.find(t => t.id === Number(raw.tipoHabitacionId));
      const room: any = {
        nombre: raw.nombre,
        tipoHabitacion: selectedType ? { id: selectedType.id } : null
      };
      this.roomSvc.create(room).subscribe({
        next: () => this.router.navigate(['/rooms/table']),
        error: (err) => console.error('Error creating room:', err)
      });
    }
  }
}
