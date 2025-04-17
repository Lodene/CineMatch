import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { WatchList } from '../../models/watchlist';
import { Movie } from '../../models/movie';

@Injectable({
  providedIn: 'root'
})
export class WatchlistService {

  backendUrl = "http://localhost:8081/watchlist"; 

  constructor(private http: HttpClient) { }

  public getCurrentUserWatchList(): Observable<Movie[]> {
    return this.http.get<Movie[]>(`${this.backendUrl}`);
  }


  /**
   * 
   * @param movieId implicit
   * @returns nothing
   */
  public addOrRemoveMovieFromWatchlist(movieId: number): Observable<void> {
    return this.http.post<void>(`${this.backendUrl}/${movieId}`, {});
  }
}
