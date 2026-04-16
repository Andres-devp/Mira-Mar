import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../../../core/services/auth.service';

@Component({
  selector: 'app-create-account',
  templateUrl: './create-account.component.html',
  styleUrls: ['./create-account.component.css']
})
export class CreateAccountComponent {
  nombre = '';
  usuario = '';
  email = '';
  contrasena = '';
  contrasenaConfirm = '';
  termsAccepted = false;
  errorMessage = '';
  successMessage = '';
  loading = false;

  constructor(
    private authService: AuthService,
    private router: Router
  ) {}

  onRegister(): void {
    this.errorMessage = '';
    this.successMessage = '';

    if (!this.termsAccepted) {
      this.errorMessage = 'Debes aceptar los términos y condiciones.';
      return;
    }

    this.loading = true;

    this.authService.register({
      nombre: this.nombre,
      usuario: this.usuario,
      email: this.email,
      contrasena: this.contrasena,
      contrasenaConfirm: this.contrasenaConfirm
    }).subscribe({
      next: (response) => {
        this.loading = false;
        this.successMessage = response.mensaje;
        setTimeout(() => this.router.navigate(['/login']), 1500);
      },
      error: (err) => {
        this.loading = false;
        this.errorMessage = err.error?.error || 'Error al crear la cuenta.';
      }
    });
  }
}
