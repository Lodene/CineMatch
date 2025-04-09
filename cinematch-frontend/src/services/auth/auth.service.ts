import { Inject, Injectable, PLATFORM_ID } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { BehaviorSubject, Observable, tap } from 'rxjs';
import { Router } from '@angular/router';
import { isPlatformBrowser } from '@angular/common';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private readonly apiUrl = 'http://localhost:8081/auth';
  private tokenSubject: BehaviorSubject<string | null> = new BehaviorSubject<string | null>(null);

  constructor(@Inject(PLATFORM_ID) private platformId: any,    
    private http: HttpClient, private router: Router,
    
  ) {
    // Check if there is a token in cookies and set it in the subject
    const token = this.getTokenFromStorage();
    this.tokenSubject.next(token);
  }

  // Get the current token from BehaviorSubject
  get currentToken(): Observable<string | null> {
    return this.tokenSubject.asObservable();
  }

  // Login method: Assuming you receive the JWT after authentication
  login(username: string, password: string): void {
    console.log(username, password);
    this.http.post<any>(`${this.apiUrl}/login`, { username, password }).subscribe((res: any) => {
      const token = res.token;
      this.setTokenInStorage(token);
      this.tokenSubject.next(token); // Update the BehaviorSubject with new token
    });
  }

  // Method to set token in HttpOnly cookie
  private setTokenInStorage(token: string): void {
    localStorage.setItem("token", token);
  }

  // Method to retrieve token from cookie
  public getTokenFromStorage(): string | null {
    return localStorage.getItem("token");
  }

  // Logout method: Clear JWT from cookie and BehaviorSubject
  logout(): void {
    this.clearTokenInStorage();
    this.tokenSubject.next(null);
    this.router.navigate(['/login']);
  }

  // Method to clear token in HttpOnly cookie
  private clearTokenInStorage(): void {
    localStorage.clear();
  }

  // Method to check if the user is authenticated
  isAuthenticated(): boolean {
    return !!this.tokenSubject.value;  // Checks if there is a token in BehaviorSubject
  }

  // Add Authorization header to the request
  addAuthHeader(headers: HttpHeaders): HttpHeaders {
    const token = this.tokenSubject.value;
    return token ? headers.append('Authorization', `Bearer ${token}`) : headers;
  }
}
