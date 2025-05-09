import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { WatchList } from '../../models/watchlist';
import { Movie } from '../../models/movie';
import { MovieConsultation } from '../../models/movieConsultation';

@Injectable({
  providedIn: 'root'
})
export class WatchlistService {

  backendUrl = "http://localhost:8081/watchlist";
  backendUrlUSer: string ='user';

  constructor(private http: HttpClient) { }

  public getCurrentUserWatchList(): Observable<MovieConsultation[]> {
    return this.http.get<MovieConsultation[]>(`${this.backendUrl}`);
  }

  public getWatchListByUsername(username: string): Observable<MovieConsultation[]> {
    return this.http.get<MovieConsultation[]>(`${this.backendUrl}/${this.backendUrlUSer}/${username}`);
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
