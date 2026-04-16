import { Component, OnInit } from '@angular/core';
import { Client } from '../../../../core/models/entities';
import { UserService } from '../../../../core/services/user.service';

@Component({
  selector: 'app-usuarios-table',
  templateUrl: './usuarios-table.component.html',
  styleUrls: ['./usuarios-table.component.css']
})
export class UsuariosTableComponent implements OnInit {
  usuarios: Client[] = [];
  warningMessage = '';

  constructor(private userService: UserService) {}

  ngOnInit(): void {
    this.loadUsuarios();
  }

  loadUsuarios(): void {
    this.userService.getAll().subscribe({
      next: (data) => this.usuarios = data,
      error: (err) => console.error('Error loading users:', err)
    });
  }

  deleteUsuario(usuario: Client): void {
    if (confirm('¿Seguro que deseas eliminar este usuario?')) {
      this.userService.delete(usuario.id).subscribe({
        next: () => {
          this.warningMessage = '';
          this.loadUsuarios();
        },
        error: (err) => {
          this.warningMessage = err.error?.error || 'Error al eliminar el usuario. Puede que tenga reservas asociadas.';
        }
      });
    }
  }
}
