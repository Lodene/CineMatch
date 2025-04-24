import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Movie } from '../../models/movie';
import { MovieConsultation } from '../../models/movieConsultation';
import { MovieSearchRequest } from '../../models/movie-search-request';
import { PaginatedMovieResponse } from '../../models/paginated-movie-reponse';


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
  public getMovieById(movieId: number): Observable<MovieConsultation> {
    return this.http.get<MovieConsultation>(`${this.backendUrl}/${movieId}`);
  }

  public getAllMovies(page: number, size: number):Observable<PaginatedMovieResponse> {
    return this.http.get<PaginatedMovieResponse>(`${this.backendUrl}/movies?page=${page}&size=${size}`);
  }

  /**
   * 
   * @returns the number of movie present in the database
   */
  public getMovieCount(): Observable<number> {
    return this.http.get<number>(`${this.backendUrl}/getMovieCount`)
  }

  public getAllGeneres() {
    return this.http.get<string[]>(`${this.backendUrl}/genres`);
  }


  public searchMovies(movieSearchRequest: MovieSearchRequest, page: number = 0, size: number = 10) {
    return this.http.post<any>(
      `${this.backendUrl}/search?page=${page}&size=${size}`, 
      movieSearchRequest
    );
  }
}
