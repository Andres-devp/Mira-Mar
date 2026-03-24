import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subject, takeUntil } from 'rxjs';
import { RoomType } from '../../../../core/models/entities';
import { RoomTypeMockService } from '../../../../core/services/room-type-mock.service';

@Component({
  selector: 'app-room-type-detail',
  templateUrl: './room-type-detail.component.html',
  styleUrls: ['./room-type-detail.component.css']
})
export class RoomTypeDetailComponent implements OnInit, OnDestroy {
  tipo?: RoomType;
  readonly nochesDemo = 3;

  private readonly destroy$ = new Subject<void>();

  constructor(
    private readonly route: ActivatedRoute,
    private readonly router: Router,
    private readonly roomTypeService: RoomTypeMockService
  ) {}

  ngOnInit(): void {
    this.route.paramMap.pipe(takeUntil(this.destroy$)).subscribe((params) => {
      const id = Number(params.get('id'));

      this.roomTypeService
        .getById(id)
        .pipe(takeUntil(this.destroy$))
        .subscribe((tipo) => {
          this.tipo = tipo;
        });
    });
  }

  editTipo(): void {
    if (!this.tipo) {
      return;
    }

    this.router.navigate(['/roomtypes', this.tipo.id, 'edit']);
  }

  get guestOptions(): number[] {
    if (!this.tipo) {
      return [1];
    }

    return Array.from({ length: this.tipo.capacidad }, (_, index) => index + 1);
  }

  formatPrice(value: number): string {
    return `$${value.toFixed(1)}`;
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }
}
