import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Movie } from '../../models/movie';
import { MovieConsultation } from '../../models/movieConsultation';


@Injectable({
  providedIn: 'root'
})
export class MovieService {

  backendUrl: string = 'http://localhost:8081/movie';

  constructor(private http: HttpClient) { }

  /**
   * 
   * @param movieId Id of the movie in the database
   * @returns Movie, 401, 404
   */ 
  public getMovieById(movieId: number):Observable<MovieConsultation> {
    return this.http.get<MovieConsultation>(`${this.backendUrl}/${movieId}`);
  }

  public getAllMovies():Observable<Movie[]> {
    return this.http.get<Movie[]>(`${this.backendUrl}/movies`);
  }

  public addMovie(movie: Movie) {
    return this.http.post<Movie>(`${this.backendUrl}`, movie, {
      headers: new HttpHeaders({
        'Content-Type': 'application/json'
      })
    });
  }
}
