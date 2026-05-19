import { Injectable } from '@angular/core';
import {
  HttpEvent,
  HttpHandler,
  HttpInterceptor,
  HttpRequest,
} from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {
  intercept(
    req: HttpRequest<unknown>,
    next: HttpHandler,
  ): Observable<HttpEvent<unknown>> {
    // Get the JWT token from localStorage
    const token = this.getToken();

    // Clone the request and add the Authorization header if token exists
    if (token && !this.isAuthEndpoint(req.url)) {
      req = req.clone({
        setHeaders: {
          Authorization: `Bearer ${token}`,
        },
      });
    }

    return next.handle(req);
  }

  private getToken(): string | null {
    try {
      const sessionData = localStorage.getItem('miramar_session');
      if (sessionData) {
        const session = JSON.parse(sessionData);
        return session.accessToken || null;
      }
    } catch (error) {
      console.error('Error getting token from localStorage:', error);
    }
    return null;
  }

  private isAuthEndpoint(url: string): boolean {
    // Don't add token to auth endpoints
    return url.includes('/auth/login') || 
           url.includes('/auth/register') ||
           url.includes('/auth/logout');
  }
}

