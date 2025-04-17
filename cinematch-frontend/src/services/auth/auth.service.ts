import { Inject, Injectable, PLATFORM_ID } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { BehaviorSubject, map, Observable, of, Subscription, tap } from 'rxjs';
import { Router } from '@angular/router';
import { DOCUMENT, isPlatformBrowser } from '@angular/common';
import { LoaderService } from '../loader/loader.service';
import { ToastrService } from 'ngx-toastr';
import { TranslateService } from '@ngx-translate/core';

// Request typing => 
export type SignupRequest = {
  username: string;
  password: string;
  email: string
}

export type LoginRequest = {
  username: string;
  password: string;
}


@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private readonly apiUrl = 'http://localhost:8081/auth';
  private tokenSubject: BehaviorSubject<string | null> = new BehaviorSubject<string | null>(null);
  private localStorage;

  constructor(
    @Inject(PLATFORM_ID) private platformId: any,
    @Inject(DOCUMENT) private document: Document,  
    private http: HttpClient, private router: Router,
    private loaderService: LoaderService,
    private toasterService: ToastrService,
    private translateService: TranslateService
  ) {
    this.localStorage = document.defaultView?.localStorage;
    // Check if there is a token in cookies and set it in the subject
    const token = this.getTokenFromStorage();
    this.tokenSubject.next(token);
  }

  // Get the current token from BehaviorSubject
  get currentToken(): Observable<string | null> {
    return this.tokenSubject.asObservable();
  }

  // Login method: Assuming you receive the JWT after authentication
  login(loginRequest: LoginRequest): void {
    this.loaderService.show();
    this.http.post<any>(`${this.apiUrl}/login`, loginRequest).subscribe(
      {
        next: (res) => {
          const token = res.token;
          this.setTokenInStorage(token);
          this.tokenSubject.next(token); // Update the BehaviorSubject with new token
          this.toasterService.success(this.translateService.instant('app.common-component.login.response.login-successful'));
        },
        error: (error) => {
          this.toasterService.error(error.error.reason, error.error.error);
        },
      }
    ).add(() => {
      this.loaderService.hide();
    });
  }

  // Method to set token in HttpOnly cookie
  private setTokenInStorage(token: string): void {
    if (!!this.localStorage) {
      this.localStorage.setItem("token", token);
    }
  }

  // Method to retrieve token from cookie
  public getTokenFromStorage(): string | null {
    if (!!this.localStorage) {
      return this.localStorage.getItem("token");
    }
    return null;
  }

  // Logout method: Clear JWT from cookie and BehaviorSubject
  logout(): void {
    this.clearTokenInStorage();
    this.tokenSubject.next(null);
    this.router.navigate(['/home']);
  }

  // Method to clear token in HttpOnly cookie
  private clearTokenInStorage(): void {
    localStorage.clear();
  }

  // Method to check if the user is authenticated
  isAuthenticated(): boolean {
    console.log(this.tokenSubject.value);
    return !!this.tokenSubject.value;  // Checks if there is a token in BehaviorSubject
  }

  // Add Authorization header to the request
  addAuthHeader(headers: HttpHeaders): HttpHeaders {
    const token = this.tokenSubject.value;
    return token ? headers.append('Authorization', `Bearer ${token}`) : headers;
  }
  signup(signupRequest: SignupRequest): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}/signup`, signupRequest).pipe(map(res => {
      return res;
    }), error => {
      console.log("error during signup: ", error);
      return error;
    });
  }
}
