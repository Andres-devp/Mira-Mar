import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, NavigationEnd, Router } from '@angular/router';
import { filter, map, mergeMap } from 'rxjs';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {
  title = 'MiraMar';
  showNavbar = true;
  showFooter = true;
  private revealObserver: IntersectionObserver | null = null;

  constructor(private router: Router, private activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.router.events
      .pipe(
        filter((event): event is NavigationEnd => event instanceof NavigationEnd),
        map(() => this.activatedRoute),
        map(route => {
          while (route.firstChild) { route = route.firstChild; }
          return route;
        }),
        mergeMap(route => route.data)
      )
      .subscribe(data => {
        this.showNavbar = data['showNavbar'] !== false;
        this.showFooter = data['showFooter'] !== false;
      });

    this.scheduleRevealSetup();

    this.router.events
      .pipe(filter((event): event is NavigationEnd => event instanceof NavigationEnd))
      .subscribe(() => {
        this.scheduleRevealSetup();
      });
  }

  private scheduleRevealSetup(): void {
    setTimeout(() => this.setupRevealAnimations(), 0);
  }

  private setupRevealAnimations(): void {
    if (typeof window === 'undefined' || typeof document === 'undefined') {
      return;
    }

    const revealElements = Array.from(
      document.querySelectorAll<HTMLElement>('.reveal, .reveal-wave')
    );

    if (!revealElements.length) {
      this.revealObserver?.disconnect();
      this.revealObserver = null;
      return;
    }

    this.revealObserver?.disconnect();
    this.revealObserver = null;

    revealElements.forEach((element) => {
      element.classList.remove('visible', 'no-anim');
    });

    if (!('IntersectionObserver' in window)) {
      revealElements.forEach((element) => element.classList.add('visible'));
      return;
    }

    this.revealObserver = new IntersectionObserver(
      (entries, observer) => {
        entries.forEach((entry) => {
          if (!entry.isIntersecting) {
            return;
          }

          entry.target.classList.add('visible');
          observer.unobserve(entry.target);
        });
      },
      { threshold: 0.08 }
    );

    revealElements.forEach((element) => {
      const rect = element.getBoundingClientRect();
      const alreadyVisible = rect.top < window.innerHeight && rect.bottom > 0;

      if (alreadyVisible) {
        element.classList.add('no-anim', 'visible');
      } else {
        this.revealObserver?.observe(element);
      }
    });
  }
}
