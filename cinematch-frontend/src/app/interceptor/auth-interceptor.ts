import { Injectable } from '@angular/core';
import { HttpInterceptor, HttpRequest, HttpHandler, HttpEvent, HttpHandlerFn, HttpErrorResponse } from '@angular/common/http';
import { catchError, Observable, throwError } from 'rxjs';
import { AuthService } from '../../services/auth/auth.service';
import { Router } from '@angular/router';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {

  constructor(private authService: AuthService,
    private router: Router
  ) {
  }

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    if (req.url.includes('auth')) {
      // when targeting auth, we must clear the token before sending it to the backend
      this.authService.logout();
      return next.handle(req);
      
    }
    const authToken = this.authService.getTokenFromStorage(); // Get token from cookie
    if (authToken) {
      const cloned = req.clone({
        setHeaders: {
          Authorization: `Bearer ${authToken}`
        }
      });
      return next.handle(cloned).pipe(
        catchError((err) => {
          if (err instanceof HttpErrorResponse) {
            if (err.status === 401) {
              // forbiden
              this.router.navigateByUrl('/login');
            }
          }
          return throwError(err)
        })
      );  // Proceed with the modified request
    }
    return next.handle(req); // Proceed without token
  }
}