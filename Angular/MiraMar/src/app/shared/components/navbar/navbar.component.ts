import { Component, HostListener, OnInit } from '@angular/core';
import { NavigationEnd, Router } from '@angular/router';
import { filter } from 'rxjs';
import { AuthService } from '../../../core/services/auth.service';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})
export class NavbarComponent implements OnInit {
  isScrolled = false;
  isNavOpen = false;
  isLandingRoute = true;
  isRoomTypeDetailRoute = false;

  /** Routes that use the "page-admin" style (solid white navbar like Thymeleaf) */
  isAdminStyleRoute = false;

  constructor(
    private router: Router,
    public authService: AuthService
  ) {}

  /**
   * Landing & room-type detail use transparent-to-solid scroll behaviour
   * (matching Thymeleaf's navbar fragment with data-default/data-scrolled logos).
   */
  get usesTransparentTheme(): boolean {
    return this.isLandingRoute || this.isRoomTypeDetailRoute;
  }

  /**
   * Apply the `.scrolled` class:
   *   - Always on admin-style routes (solid white navbar)
   *   - On transparent-theme routes only after scrolling
   */
  get applyScrolledStyle(): boolean {
    return this.isAdminStyleRoute || (this.usesTransparentTheme && this.isScrolled);
  }

  /**
   * Logo swap matching Thymeleaf behaviour:
   *   - Transparent theme: white logo by default, coloured logo on scroll
   *   - Admin-style routes: always the coloured logo (like Thymeleaf admin.html)
   */
  get logoPath(): string {
    if (this.isAdminStyleRoute) {
      return '/images/Mira Mar logo.png';
    }
    if (!this.usesTransparentTheme) {
      return '/images/Mira Mar logo Blanco.png';
    }
    return this.isScrolled ? '/images/Mira Mar logo.png' : '/images/Mira Mar logo Blanco.png';
  }

  /**
   * User icon swap matching Thymeleaf behaviour:
   *   - Transparent theme: white icon by default, dark icon on scroll
   *   - Admin-style routes: always dark icon
   */
  get userIconPath(): string {
    if (this.isAdminStyleRoute) {
      return '/images/usuarioNegro.png';
    }
    if (!this.usesTransparentTheme) {
      return '/images/usuarioBlanco.png';
    }
    return this.isScrolled ? '/images/usuarioNegro.png' : '/images/usuarioBlanco.png';
  }

  ngOnInit(): void {
    this.updateRouteState(this.router.url);

    this.router.events
      .pipe(
        filter((event): event is NavigationEnd => event instanceof NavigationEnd)
      )
      .subscribe((event) => {
        this.updateRouteState(event.urlAfterRedirects);
        this.isNavOpen = false;
      });
  }

  @HostListener('window:scroll')
  onScroll(): void {
    this.isScrolled = window.scrollY > 50;
  }

  @HostListener('window:resize')
  onResize(): void {
    if (window.innerWidth > 768) {
      this.isNavOpen = false;
    }
  }

  toggleMenu(): void {
    this.isNavOpen = !this.isNavOpen;
  }

  closeMenu(): void {
    this.isNavOpen = false;
  }

  logout(): void {
    this.authService.logout();
    this.router.navigate(['/']);
    this.closeMenu();
  }

  private updateRouteState(url: string): void {
    const cleanUrl = url.split('?')[0].split('#')[0];
    this.isLandingRoute = cleanUrl === '/';
    this.isRoomTypeDetailRoute = /^\/roomtypes\/\d+$/.test(cleanUrl);

    // Admin-style routes use solid white navbar (matching Thymeleaf's page-admin class)
    this.isAdminStyleRoute =
      cleanUrl.startsWith('/admin') ||
      cleanUrl === '/login' ||
      cleanUrl === '/register' ||
      cleanUrl === '/roomtypes' ||
      cleanUrl === '/roomtypes/new' ||
      /^\/roomtypes\/\d+\/edit$/.test(cleanUrl) ||
      cleanUrl === '/services' ||
      cleanUrl === '/services/new' ||
      /^\/services\/\d+\/edit$/.test(cleanUrl) ||
      /^\/services\/\d+$/.test(cleanUrl);

    this.isScrolled = window.scrollY > 50;
  }
}
