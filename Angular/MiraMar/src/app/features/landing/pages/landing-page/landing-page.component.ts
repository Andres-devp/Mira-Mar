import { Component, OnDestroy, OnInit } from '@angular/core';
import { Subject, takeUntil } from 'rxjs';
import { RoomType } from '../../../../core/models/entities';
import { RoomTypeMockService } from '../../../../core/services/room-type-mock.service';

@Component({
  selector: 'app-landing-page',
  templateUrl: './landing-page.component.html',
  styleUrls: ['./landing-page.component.css']
})
export class LandingPageComponent implements OnInit, OnDestroy {
  tiposDestacados: RoomType[] = [];

  private readonly destroy$ = new Subject<void>();

  constructor(private readonly roomTypeService: RoomTypeMockService) {}

  ngOnInit(): void {
    this.roomTypeService
      .getFeatured(3)
      .pipe(takeUntil(this.destroy$))
      .subscribe((tipos) => {
        this.tiposDestacados = tipos;
      });
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }
}
