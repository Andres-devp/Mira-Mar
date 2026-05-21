import { Component, HostListener, OnInit } from '@angular/core';
import { NavigationEnd, Router } from '@angular/router';
import { filter } from 'rxjs';
import { AuthService } from '../../../core/services/auth.service';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css'],
})
export class NavbarComponent implements OnInit {
  isScrolled = false;
  isNavOpen = false;
  isLandingRoute = true;
  isRoomTypeDetailRoute = false;
  isRoomTypeCrudRoute = false;
  isServicesCrudRoute = false;
  isRoomsPublicRoute = false;
  isServicesPublicRoute = false;
  isServiceDetailRoute = false;
  isAuthenticated = false;
  sessionUserRole = '';

  constructor(
    private router: Router,
    private authService: AuthService,
  ) {}

  get usesTransparentTheme(): boolean {
    return (
      this.isLandingRoute ||
      this.isRoomTypeDetailRoute ||
      this.isServiceDetailRoute
    );
  }

  get usesSolidTheme(): boolean {
    return (
      this.isRoomTypeCrudRoute ||
      this.isServicesCrudRoute ||
      this.isRoomsPublicRoute ||
      this.isServicesPublicRoute
    );
  }

  get applyScrolledStyle(): boolean {
    if (this.usesSolidTheme) return true;
    if (!this.usesTransparentTheme) return true;
    return this.isScrolled;
  }

  get logoPath(): string {
    if (this.usesTransparentTheme && !this.isScrolled) {
      return '/images/Mira Mar logo Blanco.png';
    }
    return '/images/Mira Mar logo.png';
  }

  isUserMenuOpen = false;

  get userIconPath(): string {
    if (this.isUserMenuOpen) return '/images/usuarioNegro.png';
    if (this.usesTransparentTheme && !this.isScrolled) return '/images/usuarioBlanco.png';
    return '/images/usuarioNegro.png';
  }

  get profileRoute(): string[] {
    if (!this.isAuthenticated) return ['/login'];
    if (this.sessionUserRole === 'CLIENT') return ['/usuarios/me'];
    if (this.sessionUserRole === 'ADMIN') return ['/admin'];
    if (this.sessionUserRole === 'OPERATOR') return ['/operator'];
    return ['/'];
  }

  ngOnInit(): void {
    this.updateRouteState(this.router.url);
    this.updateAuthState();

    this.router.events
      .pipe(filter((event): event is NavigationEnd => event instanceof NavigationEnd))
      .subscribe((event) => {
        this.updateRouteState(event.urlAfterRedirects);
        this.updateAuthState();
        this.isNavOpen = false;
      });
  }

  @HostListener('window:scroll')
  onScroll(): void {
    this.isScrolled = window.scrollY > 25;
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
    const openedMenus = document.querySelectorAll<HTMLDetailsElement>('.navbar-user-menu[open]');
    openedMenus.forEach((menu) => { menu.open = false; });
  }

  logout(): void {
    this.authService.logout().subscribe(() => {
      this.updateAuthState();
      this.closeMenu();
      this.router.navigate(['/']);
    });
  }

  private updateRouteState(url: string): void {
    const cleanUrl = url.split('?')[0].split('#')[0];

    this.isLandingRoute = cleanUrl === '/';

    this.isRoomTypeDetailRoute =
      /^\/roomtypes\/\d+$/.test(cleanUrl) || /^\/rooms\/\d+$/.test(cleanUrl);

    this.isRoomTypeCrudRoute =
      cleanUrl === '/roomtypes' ||
      cleanUrl === '/roomtypes/new' ||
      /^\/roomtypes\/\d+\/edit$/.test(cleanUrl);

    this.isServicesCrudRoute =
      cleanUrl === '/services/table' ||
      cleanUrl === '/services/new' ||
      /^\/services\/\d+\/edit$/.test(cleanUrl) ||
      cleanUrl === '/reservations' ||
      cleanUrl === '/reservations/add' ||
      /^\/reservations\/\d+$/.test(cleanUrl) ||
      cleanUrl === '/usuarios' ||
      cleanUrl === '/usuarios/add' ||
      /^\/usuarios\/edit\/\d+$/.test(cleanUrl) ||
      /^\/usuarios\/\d+$/.test(cleanUrl) ||
      cleanUrl === '/operadores' ||
      cleanUrl === '/rooms/table' ||
      cleanUrl === '/rooms/add';

    this.isRoomsPublicRoute = cleanUrl === '/rooms';
    this.isServicesPublicRoute = cleanUrl === '/services';
    this.isServiceDetailRoute = /^\/services\/\d+$/.test(cleanUrl);

    this.isScrolled = window.scrollY > 25;
  }

  private updateAuthState(): void {
    this.isAuthenticated = this.authService.isLoggedIn();
    this.sessionUserRole = this.authService.getRole() ?? '';
  }
}