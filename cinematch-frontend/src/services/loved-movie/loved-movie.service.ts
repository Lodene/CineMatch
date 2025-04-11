import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Movie } from '../../models/movie';

@Injectable({
  providedIn: 'root'
})
export class LovedMovieService {

  backendUrl: string = 'http://localhost:8081/loved-movies';

  constructor(private http: HttpClient) { }

  public getCurrentUserLikedMovie(): Observable<Movie[]> {
    return this.http.get<Movie[]>(`${this.backendUrl}`)
  }
  public LikeOrUnlikeMovie(movieId: number): Observable<any> {
    return this.http.post(`${this.backendUrl}/${movieId}`, {});
  }
  public getLovedMoviesByUsername(username: string): Observable<Movie[]> {
    return this.http.get<Movie[]>(`${this.backendUrl}/${username}`);
  }
}

