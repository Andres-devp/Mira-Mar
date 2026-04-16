import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
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
  currentId: number | null = null;
  pageTitle = 'Nueva Habitación';
  submitLabel = 'Crear habitación';

  constructor(
    private fb: FormBuilder,
    private route: ActivatedRoute,
    private rtSvc: RoomTypeService,
    private roomSvc: RoomService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.roomForm = this.fb.group({
      nombre: ['', Validators.required],
      tipoHabitacionId: ['', Validators.required]
    });

    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.isNew = false;
      this.currentId = Number(id);
      this.pageTitle = 'Editar Habitación';
      this.submitLabel = 'Guardar cambios';
      this.loadRoom(this.currentId);
    }

    this.rtSvc.getAll().subscribe({
      next: (data) => {
        this.roomTypes = data;
        this.onTipoChange();
      },
      error: (err) => console.error('Error loading room types:', err)
    });
  }

  loadRoom(id: number): void {
    this.roomSvc.getById(id).subscribe({
      next: (room) => {
        this.roomForm.patchValue({
          nombre: room.nombre,
          tipoHabitacionId: room.tipoHabitacion?.id ?? room.tipoHabitacionId ?? ''
        });
        this.onTipoChange();
      },
      error: (err) => {
        console.error('Error loading room:', err);
        this.router.navigate(['/rooms/table']);
      }
    });
  }

  onTipoChange(): void {
    const selectedId = Number(this.roomForm.value.tipoHabitacionId);
    this.selectedType = this.roomTypes.find(t => t.id === selectedId);
  }

  save(): void {
    if (this.roomForm.invalid) {
      this.roomForm.markAllAsTouched();
      return;
    }

    const raw = this.roomForm.value;
    const selectedType = this.roomTypes.find(t => t.id === Number(raw.tipoHabitacionId));
    const room: any = {
      nombre: raw.nombre,
      tipoHabitacion: selectedType ? { id: selectedType.id } : null
    };

    if (this.isNew) {
      this.roomSvc.create(room).subscribe({
        next: () => this.router.navigate(['/rooms/table']),
        error: (err) => console.error('Error creating room:', err)
      });
      return;
    }

    if (this.currentId === null) {
      return;
    }

    this.roomSvc.update(this.currentId, room).subscribe({
      next: () => this.router.navigate(['/rooms/table']),
      error: (err) => console.error('Error updating room:', err)
    });
  }
}
