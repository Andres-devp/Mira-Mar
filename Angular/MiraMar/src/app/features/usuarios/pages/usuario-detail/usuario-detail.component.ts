import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Client, Reservation, RoomType } from '../../../../core/models/entities';
import { ReservationService } from '../../../../core/services/reservation.service';
import { RoomTypeService } from '../../../../core/services/room-type.service';
import { UserService } from '../../../../core/services/user.service';
import { AuthService } from '../../../../core/services/auth.service';

@Component({
  selector: 'app-usuario-detail',
  templateUrl: './usuario-detail.component.html',
  styleUrls: ['./usuario-detail.component.css']
})
export class UsuarioDetailComponent implements OnInit {
  usuario?: Client;
  reservations: Reservation[] = [];
  roomTypes: RoomType[] = [];

  showEditModal = false;
  editingReservation?: Reservation;
  editForm: FormGroup;
  editLoading = false;
  editError = '';

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private fb: FormBuilder,
    private userService: UserService,
    private reservationService: ReservationService,
    private roomTypeService: RoomTypeService,
    private authService: AuthService 
  ) {
    this.editForm = this.fb.group({
      roomTypeId: [null, [Validators.required]],
      fechaInicio: ['', Validators.required],
      fechaFin: ['', Validators.required],
      cantidadPersonas: [1, [Validators.required, Validators.min(1)]]
    });
  }

  ngOnInit(): void {
    this.roomTypeService.getAll().subscribe({
      next: (data) => { this.roomTypes = data; },
      error: () => {}
    });

    this.route.paramMap.subscribe((params) => {
      const paramId = params.get('id');
      const id = paramId ? Number(paramId) : this.authService.getUserId();
      if (!id) return;
      this.userService.getById(id).subscribe({
        next: (data) => {
          this.usuario = data;
        this.loadReservations(id);
        },
        error: (err) => console.error('Error loading user:', err)
      });
    });
  }
  
  loadReservations(userId: number): void {
    this.reservationService.getByClient(userId).subscribe({
      next: (data) => { this.reservations = data; },
      error: (err) => console.error('Error loading reservations:', err)
    });
  }

  get selectedCapacidad(): number {
    const id = Number(this.editForm.get('roomTypeId')?.value);
    return this.roomTypes.find(r => r.id === id)?.capacidad || 99;
  }

  openEditModal(reservation: Reservation): void {
    this.editingReservation = reservation;
    this.editError = '';
    this.editForm.reset({
      roomTypeId: reservation.room?.tipoHabitacion?.id ?? null,
      fechaInicio: reservation.fechaInicio,
      fechaFin: reservation.fechaFin,
      cantidadPersonas: reservation.cantidadPersonas
    });
    this.showEditModal = true;
  }

  closeEditModal(): void {
    this.showEditModal = false;
    this.editingReservation = undefined;
    this.editError = '';
  }

  saveEdit(): void {
    if (this.editForm.invalid || !this.editingReservation) {
      this.editForm.markAllAsTouched();
      return;
    }
    const { roomTypeId, fechaInicio, fechaFin, cantidadPersonas } = this.editForm.getRawValue();
    if (Number(cantidadPersonas) > this.selectedCapacidad) {
      this.editError = `La cantidad de personas no puede superar ${this.selectedCapacidad}.`;
      return;
    }
    this.editLoading = true;
    this.editError = '';
    this.reservationService.updateReservacion(
      this.editingReservation.id,
      Number(roomTypeId),
      fechaInicio,
      fechaFin,
      Number(cantidadPersonas)
    ).subscribe({
      next: (updated) => {
        this.editLoading = false;
        const idx = this.reservations.findIndex(r => r.id === updated.id);
        if (idx !== -1) this.reservations[idx] = updated;
        this.closeEditModal();
      },
      error: (err) => {
        this.editLoading = false;
        this.editError = err.error?.error || 'No se pudo actualizar la reserva.';
      }
    });
  }

  deleteAccount(): void {
    if (!this.usuario) return;
    const confirmed = confirm('¿Seguro que deseas eliminar esta cuenta? Esta acción no se puede deshacer.');
    if (confirmed) {
      this.userService.delete(this.usuario.id).subscribe({
        next: () => { this.router.navigate(['/usuarios']); },
        error: (err) => {
          alert(err.error?.error || 'Error al eliminar la cuenta. Puede que tenga reservas asociadas.');
        }
      });
    }
  }
}
