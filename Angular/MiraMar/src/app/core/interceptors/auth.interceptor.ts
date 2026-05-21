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
    const token = this.getToken();

    let clonedReq = req;

    if (token && !this.isAuthEndpoint(req.url)) {
      clonedReq = req.clone({
        setHeaders: {
          Authorization: `Bearer ${token}`,
        },
        withCredentials: true,
      });
    } else if (!this.isAuthEndpoint(req.url)) {
      clonedReq = req.clone({
        withCredentials: true,
      });
    }

    return next.handle(clonedReq);
  }

  private getToken(): string | null {
    return localStorage.getItem('miramar_token');
  }

  private isAuthEndpoint(url: string): boolean {
    return url.includes('/auth/login') ||
           url.includes('/auth/register') ||
           url.includes('/auth/logout');
  }
}