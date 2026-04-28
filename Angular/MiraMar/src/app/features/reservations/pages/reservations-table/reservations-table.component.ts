import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
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

  searchId: number | null = null;
  searchError = '';

  constructor(
    private reservationService: ReservationService,
    private router: Router
  ) {}

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

  searchById(): void {
    this.searchError = '';
    const id = Number(this.searchId);
    if (!id || id <= 0) {
      this.searchError = 'Ingresa un ID de reserva válido.';
      return;
    }
    this.reservationService.getById(id).subscribe({
      next: () => {
        this.router.navigate(['/reservations', id]);
      },
      error: () => {
        this.searchError = `No se encontró ninguna reserva con ID ${id}.`;
      }
    });
  }
}
