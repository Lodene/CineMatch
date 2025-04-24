import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class MovieRecommendationService {

  private http = inject(HttpClient)
  backendUrl: string = "http://localhost:8081";

  constructor() { }

  public getRecommendedMovie(): Observable<string> {
    return this.http.get<string>(`${this.backendUrl}/similar-movie`);
  }

}
