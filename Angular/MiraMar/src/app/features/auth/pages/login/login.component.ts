import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../../../core/services/auth.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
})
export class LoginComponent {
  username = '';
  password = '';
  errorMessage = '';
  loading = false;

  constructor(
    private authService: AuthService,
    private router: Router,
  ) {}

  onLogin(): void {
    this.errorMessage = '';
    this.loading = true;

    this.authService
      .login({ username: this.username, password: this.password })
      .subscribe({
        next: (response) => {
          this.loading = false;
          switch (response.role) {
            case 'ADMIN':
              this.router.navigate(['/admin']);
              break;
            case 'OPERATOR':
              this.router.navigate(['/operator']);
              break;
            case 'CLIENT':
              this.router.navigate(['/usuarios/me']);
              break;
            default:
              this.errorMessage = 'Rol desconocido';
          }
        },
        error: (err) => {
          this.loading = false;
          this.errorMessage =
            err.error?.error || 'Usuario o contraseña incorrectos';
        },
      });
  }
}
