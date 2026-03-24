import { Component, OnDestroy, OnInit } from '@angular/core';
import { Subject, takeUntil } from 'rxjs';
import { RoomType } from '../../../../core/models/entities';
import { RoomTypeMockService } from '../../../../core/services/room-type-mock.service';

@Component({
  selector: 'app-room-type-list',
  templateUrl: './room-type-list.component.html',
  styleUrls: ['./room-type-list.component.css']
})
export class RoomTypeListComponent implements OnInit, OnDestroy {
  tiposHabitacion: RoomType[] = [];
  warningMessage = '';

  private readonly destroy$ = new Subject<void>();

  constructor(private readonly roomTypeService: RoomTypeMockService) {}

  ngOnInit(): void {
    this.roomTypeService
      .getAll()
      .pipe(takeUntil(this.destroy$))
      .subscribe((tipos) => {
        this.tiposHabitacion = tipos;
      });
  }

  deleteTipo(type: RoomType): void {
    const confirmed = window.confirm('¿Seguro que deseas eliminar este tipo?');

    if (!confirmed) {
      return;
    }

    const result = this.roomTypeService.delete(type.id);
    this.warningMessage = result.success ? '' : result.message || '';
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }
}
