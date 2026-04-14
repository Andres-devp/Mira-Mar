import { Component, OnInit } from '@angular/core';
import { Operator } from '../../../../core/models/entities';
import { OperatorServiceService } from '../../../../core/services/operator.service';

@Component({
  selector: 'app-operator-list',
  templateUrl: './operator-list.component.html',
  styleUrls: ['./operator-list.component.css']
})
export class OperatorListComponent implements OnInit {
  operators: Operator[] = [];
  warningMessage = '';

  constructor(private operatorService: OperatorServiceService) {}

  ngOnInit(): void {
    this.loadOperators();
  }

  loadOperators(): void {
    this.operatorService.findAll().subscribe((operators) => {
      this.operators = operators;
    });
  }

  deleteOperator(operator: Operator): void {
    const confirmed = window.confirm(`¿Seguro que deseas eliminar a ${operator.nombre}?`);
    if (!confirmed) return;

    this.operatorService.delete(operator.id).subscribe({
      next: () => {
        this.warningMessage = '';
        this.loadOperators();
      },
      error: (err) => {
        this.warningMessage = err.error?.error || 'Error al eliminar el operador';
      }
    });
  }
}
