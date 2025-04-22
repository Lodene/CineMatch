import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Review } from '../../models/review';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ReviewService {

  backendUrl: string = 'http://localhost:8081/reviews';

  constructor(private http: HttpClient) { }

  public getUserReviews(): Observable<Review[]> {
    return this.http.get<Review[]>(`${this.backendUrl}`);
  }
  
  public deleteReview(reviewId: number): Observable<boolean> {
    return this.http.delete<boolean>(`${this.backendUrl}/${reviewId}`);
  }

  public updateReview(reviewId: number, review: Review): Observable<Review> {
    return this.http.put<Review>(`${this.backendUrl}/${reviewId}`, review);
  }

  public createReview(review: Review): Observable<Review> {
    return this.http.post<Review>(`${this.backendUrl}`, review);
  }
// fixme: not implemented yet
  public getReviewByUsername(username: string): Observable<Review[]> {
    return this.http.get<Review[]>(`${this.backendUrl}/getByUsername/${username}`);
  }

}
