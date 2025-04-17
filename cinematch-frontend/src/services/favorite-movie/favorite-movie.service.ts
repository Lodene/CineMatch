import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class FavoriteMovieService {

  backendUrl: string = "http://localhost:8081/loved-movies"; 

  constructor(private http: HttpClient) { }


  public likeOrUnlikeMovie(movieId: number): Observable<any> {
    return this.http.post<any>(`${this.backendUrl}/${movieId}`, {});
  } 

}
