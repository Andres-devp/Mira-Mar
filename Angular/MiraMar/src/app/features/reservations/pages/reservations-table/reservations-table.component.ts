import { Component, OnInit } from '@angular/core';
import { Reservation } from '../../../../core/models/entities';
import { ReservationService } from '../../../../core/services/reservation.service';

@Component({
  selector: 'app-reservations-table',
  templateUrl: './reservations-table.component.html',
  styleUrls: ['./reservations-table.component.css']
})
export class ReservationsTableComponent implements OnInit {
  reservas: Reservation[] = [];
  warningMessage = '';

  constructor(private reservationService: ReservationService) {}

  ngOnInit(): void {
    this.loadReservas();
  }

  loadReservas(): void {
    this.reservationService.getAll().subscribe({
      next: (data) => {
        this.warningMessage = '';
        this.reservas = data;
      },
      error: (err) => {
        console.error('Error loading reservations:', err);
        this.warningMessage = 'No se pudieron cargar las reservas.';
      }
    });
  }
}
