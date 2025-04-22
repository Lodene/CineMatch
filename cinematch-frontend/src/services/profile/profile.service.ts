import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { User } from '../../models/types/components/user/user.model';

@Injectable({
  providedIn: 'root'
})
export class ProfileService {

  backendUrl: string = 'http://localhost:8081/profile';

  public usernameSubject: BehaviorSubject<string | null> = new BehaviorSubject<string | null>(null);
  

  constructor(private http: HttpClient) { }

  /**
   * Return current userProfile
   */
  public getProfil(): Observable<User> {
    return this.http.get<User>(`${this.backendUrl}`);
  }

  public getProfileByUsername(username: string): Observable<User> {
    return this.http.get<User>(`${this.backendUrl}/${username}`);
  }

  /**
   * 
   * @param profileInfo New user profile (also update picture)
   * @returns new profile
   */
  public updateProfile(profileInfo: User): Observable<User> {
    return this.http.put<User>(`${this.backendUrl}`, profileInfo);
  }

  /**
   * 
   * @param picturePath new picture path
   * @returns new picture path
   */
  public updatePicture(picturePath: string): Observable<any> {
    return this.http.put(`${this.backendUrl}/picture`, picturePath);
  }

  public deletePicture(): Observable<any> {
    return this.http.delete(`${this.backendUrl}/picture`);
  }

  // Get the current token from BehaviorSubject
  public get currentUsername(): Observable<string | null> {
    return this.usernameSubject.asObservable();
  }

}
