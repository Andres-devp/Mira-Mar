import { Component, OnInit } from '@angular/core';
import { Client } from '../../../../core/models/entities';
import { ClientService } from '../../../../core/services/client.service';

@Component({
  selector: 'app-user-list',
  templateUrl: './user-list.component.html',
  styleUrls: ['./user-list.component.css']
})
export class UserListComponent implements OnInit {
  clientes: Client[] = [];
  warningMessage = '';

  constructor(private clientService: ClientService) {}

  ngOnInit(): void {
    this.loadClientes();
  }

  loadClientes(): void {
    this.clientService.findAll().subscribe((clientes) => {
      this.clientes = clientes;
    });
  }

  deleteCliente(client: Client): void {
    const confirmed = window.confirm(`¿Seguro que deseas eliminar a ${client.nombre}?`);
    if (!confirmed) return;

    this.clientService.delete(client.id).subscribe({
      next: () => {
        this.warningMessage = '';
        this.loadClientes();
      },
      error: (err) => {
        this.warningMessage = err.error?.error || 'Error al eliminar el cliente';
      }
    });
  }
}
