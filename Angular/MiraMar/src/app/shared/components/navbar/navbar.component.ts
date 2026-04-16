import { Component, HostListener, OnInit } from '@angular/core';
import { NavigationEnd, Router } from '@angular/router';
import { filter } from 'rxjs';

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
  isRoomTypeCrudRoute = false;
  isServicesCrudRoute = false;
  isRoomsPublicRoute = false;
  isServicesPublicRoute = false;
  isServiceDetailRoute = false;

  constructor(private router: Router) {}

  /** Pages that start transparent navbar and become solid on scroll */
  get usesTransparentTheme(): boolean {
    return this.isLandingRoute || this.isRoomTypeDetailRoute || this.isServiceDetailRoute;
  }

  /** Pages that always use a solid dusk-blue navbar */
  get usesSolidTheme(): boolean {
    return this.isRoomTypeCrudRoute || this.isServicesCrudRoute
      || this.isRoomsPublicRoute || this.isServicesPublicRoute;
  }

  get applyScrolledStyle(): boolean {
    if (this.usesSolidTheme) return true;
    return this.usesTransparentTheme && this.isScrolled;
  }

  get logoPath(): string {
    if (this.usesSolidTheme) return '/images/Mira Mar logo Blanco.png';
    if (!this.usesTransparentTheme) return '/images/Mira Mar logo Blanco.png';
    return this.isScrolled ? '/images/Mira Mar logo.png' : '/images/Mira Mar logo Blanco.png';
  }

  get userIconPath(): string {
    if (this.usesSolidTheme) return '/images/usuarioBlanco.png';
    if (!this.usesTransparentTheme) return '/images/usuarioBlanco.png';
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
  }

  private updateRouteState(url: string): void {
    const cleanUrl = url.split('?')[0].split('#')[0];

    this.isLandingRoute = cleanUrl === '/';

    this.isRoomTypeDetailRoute =
      /^\/roomtypes\/\d+$/.test(cleanUrl) ||
      /^\/rooms\/\d+$/.test(cleanUrl);

    this.isRoomTypeCrudRoute =
      cleanUrl === '/roomtypes' ||
      cleanUrl === '/roomtypes/new' ||
      /^\/roomtypes\/\d+\/edit$/.test(cleanUrl);

    this.isServicesCrudRoute =
      cleanUrl === '/services/table' ||
      cleanUrl === '/services/new' ||
      /^\/services\/\d+\/edit$/.test(cleanUrl) ||
      cleanUrl === '/reservations' ||
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
}
