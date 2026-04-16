import { Component, OnInit } from '@angular/core';
import { Operator } from '../../../../core/models/entities';
import { OperatorService } from '../../../../core/services/operator.service';

@Component({
  selector: 'app-operadores-table',
  templateUrl: './operadores-table.component.html',
  styleUrls: ['./operadores-table.component.css']
})
export class OperadoresTableComponent implements OnInit {
  operadores: Operator[] = [];

  constructor(private operatorService: OperatorService) {}

  ngOnInit(): void {
    this.loadOperadores();
  }

  loadOperadores(): void {
    this.operatorService.getAll().subscribe({
      next: (data) => this.operadores = data,
      error: (err) => console.error('Error loading operators:', err)
    });
  }
}
