import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, map } from 'rxjs';
import { Movie } from '../../models/movie';
import { MovieConsultation } from '../../models/movieConsultation';
import { MovieSearchRequest } from '../../models/movie-search-request';
import { PaginatedMovieResponse } from '../../models/paginated-movie-reponse';

@Injectable({
  providedIn: 'root'
})
export class MovieService {

  backendUrl: string = 'http://localhost:8081/';
  tmdbBaseUrl: string = 'https://api.themoviedb.org/3';
  tmdbApiKey: string = '87161c039b9e1323d0366432f76803d7'; // 🔒 Remplace par ta clé API TMDB

  constructor(private http: HttpClient) {}

  /**
   * @param movieId Id of the movie in the database
   * @returns Movie, 401, 404
   */
  public getMovieById(movieId: number): Observable<MovieConsultation> {
    return this.http.get<MovieConsultation>(`${this.backendUrl}movie/${movieId}`);
  }

  public getAllMovies(page: number, size: number): Observable<PaginatedMovieResponse> {
    return this.http.get<PaginatedMovieResponse>(`${this.backendUrl}movie/movies?page=${page}&size=${size}`);
  }

  /**
   * @returns the number of movies present in the database
   */
  public getMovieCount(): Observable<number> {
    return this.http.get<number>(`${this.backendUrl}movie/getMovieCount`);
  }

  /**
   * @param tmdbMovieId L'identifiant TMDB du film
   * @returns La clé de la bande-annonce YouTube ou null
   */
  public getTrailerUrl(movieId: number): Observable<string> {
    return this.http.get(`${this.backendUrl}movie/trailer/${movieId}`, { responseType: 'text' });
  }

  public getAllGenres() {
    return this.http.get<string[]>(`${this.backendUrl}movie/genres`);
  }


  public searchMovies(movieSearchRequest: MovieSearchRequest, page: number = 0, size: number = 10) {
    return this.http.post<any>(
      `${this.backendUrl}movie/search?page=${page}&size=${size}`,
      movieSearchRequest
    );
  }

  public likeMultipleMovies(movieIds: number[]): Observable<void> {
    return this.http.post<void>('http://localhost:8081/loved-movies/bulk', movieIds);
  }

  public getSimilarMovie(movieId: number): Observable<Movie[]> {
    return this.http.get<Movie[]>(`${this.backendUrl}movie/related-movies/${movieId}`);
  }
}
