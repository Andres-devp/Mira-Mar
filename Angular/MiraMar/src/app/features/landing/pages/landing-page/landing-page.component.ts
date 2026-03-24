import { Component, OnInit } from '@angular/core';
import { RoomType } from '../../../../core/models/entities';
import { RoomTypeMockService } from '../../../../core/services/room-type-mock.service';

@Component({
  selector: 'app-landing-page',
  templateUrl: './landing-page.component.html',
  styleUrls: ['./landing-page.component.css']
})
export class LandingPageComponent implements OnInit {
  tiposDestacados: RoomType[] = [];

  constructor(private roomTypeService: RoomTypeMockService) {}

  ngOnInit(): void {
    this.tiposDestacados = this.roomTypeService.listarDestacados(3);
  }
}
