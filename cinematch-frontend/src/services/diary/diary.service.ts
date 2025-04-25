import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Movie } from '../../models/movie';
import { MovieConsultation } from '../../models/movieConsultation';

@Injectable({
  providedIn: 'root'
})
export class DiaryService {

  backendUrl: string = 'http://localhost:8081/watched-movie';
  backendUrlUSer: string ='user';

  constructor(private http: HttpClient) { }

  public getCurrentUserWatchedMovie(): Observable<MovieConsultation[]> {
    return this.http.get<MovieConsultation[]>(`${this.backendUrl}`)
  }
  public watchOrUnwatchMovie(movieId: number): Observable<any> {
    return this.http.post(`${this.backendUrl}/${movieId}`, {});
  }
  public getWatchedMoviesByUsername(username: string): Observable<MovieConsultation[]> {
    return this.http.get<MovieConsultation[]>(`${this.backendUrl}/${this.backendUrlUSer}/${username}`);
  }
}

