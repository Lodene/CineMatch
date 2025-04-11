import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Movie } from '../../models/movie';

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
  public getMovieById(movieId: number):Observable<Movie> {
    return this.http.get<Movie>(`${this.backendUrl}/${movieId}`);
  }
}
