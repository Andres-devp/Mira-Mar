import { Component, OnInit } from '@angular/core';
import { Reservation } from '../../../../core/models/entities';
import { ReservationService } from '../../../../core/services/reservation.service';

@Component({
  selector: 'app-reservation-list',
  templateUrl: './reservation-list.component.html',
  styleUrls: ['./reservation-list.component.css']
})
export class ReservationListComponent implements OnInit {
  reservations: Reservation[] = [];
  warningMessage = '';

  constructor(private reservationService: ReservationService) {}

  ngOnInit(): void {
    this.loadReservations();
  }

  loadReservations(): void {
    this.reservationService.findAll().subscribe((reservations) => {
      this.reservations = reservations;
    });
  }

  deleteReservation(reservation: Reservation): void {
    const confirmed = window.confirm('¿Seguro que deseas eliminar esta reservación?');
    if (!confirmed) return;

    this.reservationService.delete(reservation.id).subscribe({
      next: () => {
        this.warningMessage = '';
        this.loadReservations();
      },
      error: (err) => {
        this.warningMessage = err.error?.error || 'Error al eliminar la reservación';
      }
    });
  }
}
