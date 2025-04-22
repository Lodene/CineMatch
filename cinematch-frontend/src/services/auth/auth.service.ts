import { Inject, Injectable, PLATFORM_ID } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { BehaviorSubject, map, Observable, of, Subscription, tap } from 'rxjs';
import { Router } from '@angular/router';
import { DOCUMENT, isPlatformBrowser } from '@angular/common';
import { LoaderService } from '../loader/loader.service';

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

type LoginResponse = {
  token: string;
  expiresIn: number;
  lang: string;
  username: string;
}


@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private readonly apiUrl = 'http://localhost:8081/auth';
  public tokenSubject: BehaviorSubject<string | null> = new BehaviorSubject<string | null>(null);
  private localStorage;

  constructor(
    @Inject(PLATFORM_ID) private platformId: any,
    @Inject(DOCUMENT) private document: Document,  
    private http: HttpClient, private router: Router,
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

  login(loginRequest: LoginRequest): Observable<LoginResponse> {
    return this.http.post<any>(`${this.apiUrl}/login`, loginRequest);
  }
  

  // Method to set token in HttpOnly cookie
  public setTokenInStorage(token: string): void {
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
    this.clearToken();
    this.router.navigate(['/home']);
  }

  // Method to clear token in HttpOnly cookie
  private clearTokenInStorage(): void {
    localStorage.clear();
  }

  clearToken(): void {
    this.clearTokenInStorage();
    this.tokenSubject.next(null);
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
