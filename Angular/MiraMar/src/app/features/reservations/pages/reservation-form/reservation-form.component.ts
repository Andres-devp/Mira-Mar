import { Component, OnInit } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Router } from '@angular/router';
import {
  ReservationCreateRequest,
  RoomType,
} from '../../../../core/models/entities';
import { ReservationService } from '../../../../core/services/reservation.service';
import { RoomTypeService } from '../../../../core/services/room-type.service';

@Component({
  selector: 'app-reservation-form',
  templateUrl: './reservation-form.component.html',
  styleUrls: ['./reservation-form.component.css'],
})
export class ReservationFormComponent implements OnInit {
  roomTypes: RoomType[] = [];
  warningMessage = '';
  loading = false;
  isClientFlow = false;

  reservationForm = this.fb.nonNullable.group({
    roomTypeId: [0, [Validators.required, Validators.min(1)]],
    fechaInicio: ['', Validators.required],
    fechaFin: ['', Validators.required],
    cantidadPersonas: [1, [Validators.required, Validators.min(1)]],
  });

  constructor(
    private fb: FormBuilder,
    private route: ActivatedRoute,
    private roomTypeService: RoomTypeService,
    private reservationService: ReservationService,
    private router: Router,
  ) {}

  ngOnInit(): void {
    this.prefillFromQueryParams();

    this.roomTypeService.getAll().subscribe({
      next: (data) => {
        this.roomTypes = data;
        const selectedFromQuery = Number(
          this.reservationForm.controls.roomTypeId.value,
        );

        if (
          selectedFromQuery > 0 &&
          data.some((roomType) => roomType.id === selectedFromQuery)
        ) {
          this.applyRoomTypeCapacity(selectedFromQuery);
          return;
        }

        if (data.length > 0) {
          this.reservationForm.controls.roomTypeId.setValue(data[0].id);
          this.applyRoomTypeCapacity(data[0].id);
        }
      },
      error: (err) => {
        console.error('Error loading room types:', err);
        this.warningMessage = 'No se pudieron cargar los tipos de habitación.';
      },
    });

    this.reservationForm.controls.roomTypeId.valueChanges.subscribe(
      (roomTypeId) => {
        this.applyRoomTypeCapacity(Number(roomTypeId));
      },
    );
  }

  get selectedRoomType(): RoomType | undefined {
    const roomTypeId = Number(this.reservationForm.controls.roomTypeId.value);
    return this.roomTypes.find((roomType) => roomType.id === roomTypeId);
  }

  save(): void {
    this.warningMessage = '';

    if (this.reservationForm.invalid) {
      this.reservationForm.markAllAsTouched();
      return;
    }

    const raw = this.reservationForm.getRawValue();
    const payload: ReservationCreateRequest = {
      roomTypeId: Number(raw.roomTypeId),
      fechaInicio: raw.fechaInicio,
      fechaFin: raw.fechaFin,
      cantidadPersonas: Number(raw.cantidadPersonas),
    };

    this.loading = true;
    this.reservationService.create(payload).subscribe({
      next: (reservation) => {
        this.loading = false;
        const targetRoute = this.isClientFlow
          ? ['/reservations/user', reservation.id]
          : ['/reservations', reservation.id];
        this.router.navigate(targetRoute);
      },
      error: (err) => {
        this.loading = false;
        if (err.status === 302 || err.status === 401) {
          this.router.navigate(['/login']);
          return;
        }
        this.warningMessage =
          err.error?.error || 'No se pudo crear la reserva.';
      },
    });
  }

  private prefillFromQueryParams(): void {
    this.isClientFlow =
      this.route.snapshot.queryParamMap.get('source') === 'client';

    const roomTypeId = Number(
      this.route.snapshot.queryParamMap.get('roomTypeId'),
    );
    const fechaInicio =
      this.route.snapshot.queryParamMap.get('fechaInicio') || '';
    const fechaFin = this.route.snapshot.queryParamMap.get('fechaFin') || '';
    const cantidadPersonas = Number(
      this.route.snapshot.queryParamMap.get('cantidadPersonas'),
    );

    if (!Number.isNaN(roomTypeId) && roomTypeId > 0) {
      this.reservationForm.controls.roomTypeId.setValue(roomTypeId);
    }
    if (fechaInicio) {
      this.reservationForm.controls.fechaInicio.setValue(fechaInicio);
    }
    if (fechaFin) {
      this.reservationForm.controls.fechaFin.setValue(fechaFin);
    }
    if (!Number.isNaN(cantidadPersonas) && cantidadPersonas > 0) {
      this.reservationForm.controls.cantidadPersonas.setValue(cantidadPersonas);
    }
  }

  private applyRoomTypeCapacity(roomTypeId: number): void {
    const roomType = this.roomTypes.find((item) => item.id === roomTypeId);
    if (!roomType) {
      return;
    }

    const currentPeople = Number(
      this.reservationForm.controls.cantidadPersonas.value,
    );
    const normalizedPeople = currentPeople > 0 ? currentPeople : 1;
    const nextPeople = Math.min(normalizedPeople, roomType.capacidad);
    this.reservationForm.controls.cantidadPersonas.setValue(nextPeople, {
      emitEvent: false,
    });
  }
}
