import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Client } from '../../../../core/models/entities';
import { UserService } from '../../../../core/services/user.service';

@Component({
  selector: 'app-usuario-detail',
  templateUrl: './usuario-detail.component.html',
  styleUrls: ['./usuario-detail.component.css']
})
export class UsuarioDetailComponent implements OnInit {
  usuario?: Client;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private userService: UserService
  ) {}

  ngOnInit(): void {
    this.route.paramMap.subscribe((params) => {
      const id = Number(params.get('id'));
      if (id) {
        this.userService.getById(id).subscribe({
          next: (data) => this.usuario = data,
          error: (err) => console.error('Error loading user:', err)
        });
      }
    });
  }

  deleteAccount(): void {
    if (!this.usuario) return;
    const confirmed = confirm('¿Seguro que deseas eliminar esta cuenta? Esta acción no se puede deshacer.');
    if (confirmed) {
      this.userService.delete(this.usuario.id).subscribe({
        next: () => {
          this.router.navigate(['/usuarios']);
        },
        error: (err) => {
          alert(err.error?.error || 'Error al eliminar la cuenta. Puede que tenga reservas asociadas.');
        }
      });
    }
  }
}
