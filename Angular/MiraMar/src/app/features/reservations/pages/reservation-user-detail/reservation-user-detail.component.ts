import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Reservation } from '../../../../core/models/entities';
import { ReservationService } from '../../../../core/services/reservation.service';

@Component({
  selector: 'app-reservation-user-detail',
  templateUrl: './reservation-user-detail.component.html',
  styleUrls: ['./reservation-user-detail.component.css'],
})
export class ReservationUserDetailComponent implements OnInit {
  reservation?: Reservation;

  constructor(
    private route: ActivatedRoute,
    private reservationService: ReservationService,
  ) {}

  ngOnInit(): void {
    this.route.paramMap.subscribe((params) => {
      const id = Number(params.get('id'));
      if (!id) {
        return;
      }

      this.reservationService.getById(id).subscribe({
        next: (data) => {
          this.reservation = data;
        },
        error: (err) => {
          console.error('Error loading reservation detail:', err);
        },
      });
    });
  }
}
