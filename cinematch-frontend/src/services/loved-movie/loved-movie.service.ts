import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Movie } from '../../models/movie';
import { MovieConsultation } from '../../models/movieConsultation';

@Injectable({
  providedIn: 'root'
})
export class LovedMovieService {

  backendUrl: string = 'http://localhost:8081/loved-movies';
  backendUrlUSer: string ='user';

  constructor(private http: HttpClient) { }

  public getCurrentUserLikedMovie(): Observable<MovieConsultation[]> {
    return this.http.get<MovieConsultation[]>(`${this.backendUrl}`)
  }
  public LikeOrUnlikeMovie(movieId: number): Observable<any> {
    return this.http.post(`${this.backendUrl}/${movieId}`, {});
  }
  public getLovedMoviesByUsername(username: string): Observable<MovieConsultation[]> {
    return this.http.get<MovieConsultation[]>(`${this.backendUrl}/${this.backendUrlUSer}/${username}`);
  }
}

