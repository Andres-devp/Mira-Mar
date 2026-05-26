import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Operator } from '../../../../core/models/entities';
import { OperatorService } from '../../../../core/services/operator.service';
import { AuthService } from '../../../../core/services/auth.service';

@Component({
  selector: 'app-operadores-table',
  templateUrl: './operadores-table.component.html',
  styleUrls: ['./operadores-table.component.css']
})
export class OperadoresTableComponent implements OnInit {
  operadores: Operator[] = [];
  isAdmin = false;

  constructor(
    private operatorService: OperatorService,
    private authService: AuthService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.isAdmin = this.authService.getRole() === 'ADMIN';
    this.loadOperadores();
  }

  loadOperadores(): void {
    this.operatorService.getAll().subscribe({
      next: (data) => this.operadores = data,
      error: (err) => console.error('Error loading operators:', err)
    });
  }

  deleteOperador(id: number): void {
    if (confirm('¿Estás seguro de que deseas eliminar este operador?')) {
      this.operatorService.delete(id).subscribe({
        next: () => this.loadOperadores(),
        error: (err) => console.error('Error deleting operator:', err)
      });
    }
  }
}
