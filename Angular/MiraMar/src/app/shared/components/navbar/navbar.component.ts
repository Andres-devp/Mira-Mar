import { Component, HostListener, OnDestroy, OnInit } from '@angular/core';
import { NavigationEnd, Router } from '@angular/router';
import { Subject, filter, takeUntil } from 'rxjs';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})
export class NavbarComponent implements OnInit, OnDestroy {
  isScrolled = false;
  isNavOpen = false;
  isLandingRoute = true;
  isRoomTypeDetailRoute = false;
  isRoomTypeCrudRoute = false;

  private readonly destroy$ = new Subject<void>();

  constructor(private readonly router: Router) {}

  get usesTransparentTheme(): boolean {
    return this.isLandingRoute || this.isRoomTypeDetailRoute;
  }

  get applyScrolledStyle(): boolean {
    return this.isRoomTypeCrudRoute || (this.usesTransparentTheme && this.isScrolled);
  }

  get logoPath(): string {
    if (!this.usesTransparentTheme) {
      return '/images/Mira Mar logo Blanco.png';
    }

    return this.isScrolled ? '/images/Mira Mar logo.png' : '/images/Mira Mar logo Blanco.png';
  }

  get userIconPath(): string {
    if (!this.usesTransparentTheme) {
      return '/images/usuarioBlanco.png';
    }

    return this.isScrolled ? '/images/usuarioNegro.png' : '/images/usuarioBlanco.png';
  }

  ngOnInit(): void {
    this.updateRouteState(this.router.url);

    this.router.events
      .pipe(
        filter((event): event is NavigationEnd => event instanceof NavigationEnd),
        takeUntil(this.destroy$)
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

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  private updateRouteState(url: string): void {
    const cleanUrl = url.split('?')[0].split('#')[0];
    this.isLandingRoute = cleanUrl === '/';
    this.isRoomTypeDetailRoute = /^\/roomtypes\/\d+$/.test(cleanUrl);
    this.isRoomTypeCrudRoute =
      cleanUrl === '/roomtypes' ||
      cleanUrl === '/roomtypes/new' ||
      /^\/roomtypes\/\d+\/edit$/.test(cleanUrl);
    this.isScrolled = window.scrollY > 25;
  }
}
