import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService, RegisterRequest } from '../../../../core/services/auth.service';

@Component({
  selector: 'app-register-page',
  templateUrl: './register-page.component.html',
  styleUrls: ['./register-page.component.css']
})
export class RegisterPageComponent {
  data: RegisterRequest = {
    nombre: '',
    usuario: '',
    email: '',
    contrasena: '',
    contrasenaConfirm: ''
  };
  errorMessage = '';
  successMessage = '';
  loading = false;

  constructor(
    private authService: AuthService,
    private router: Router
  ) {}

  register(): void {
    if (!this.data.nombre || !this.data.usuario || !this.data.email || !this.data.contrasena) {
      this.errorMessage = 'Completa todos los campos obligatorios';
      return;
    }

    if (this.data.contrasena !== this.data.contrasenaConfirm) {
      this.errorMessage = 'Las contraseñas no coinciden';
      return;
    }

    this.loading = true;
    this.errorMessage = '';
    this.successMessage = '';

    this.authService.register(this.data).subscribe({
      next: () => {
        this.loading = false;
        this.successMessage = 'Cuenta creada exitosamente. Redirigiendo...';
        setTimeout(() => this.router.navigate(['/login']), 1500);
      },
      error: (err) => {
        this.loading = false;
        this.errorMessage = err.error?.error || 'Error al registrar la cuenta';
      }
    });
  }
}
