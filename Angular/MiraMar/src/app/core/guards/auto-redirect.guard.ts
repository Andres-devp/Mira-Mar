import { Injectable } from '@angular/core';
import { CanActivate, Router } from '@angular/router';
import { AuthService } from '../services/auth.service';

@Injectable({
  providedIn: 'root'
})
export class AutoRedirectGuard implements CanActivate {
  constructor(private authService: AuthService, private router: Router) {}

  canActivate(): boolean {
    if (this.authService.isLoggedIn()) {
      const role = this.authService.getRole();
      if (role === 'ADMIN') {
        this.router.navigate(['/admin']);
      } else if (role === 'OPERATOR') {
        this.router.navigate(['/operator']);
      } else if (role === 'CLIENT') {
        this.router.navigate(['/usuarios/me']);
      }
      return false;
    }
    // Si no hay sesión, permite acceder al landing page
    return true;
  }
}
