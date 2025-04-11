import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Profile } from '../../models/profile';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ProfileService {

  backendUrl: string = 'http://localhost:8081/profile';

  constructor(private http: HttpClient) { }

  /**
   * Return current userProfile
   */
  public getProfil(): Observable<Profile> {
    return this.http.get<Profile>(`${this.backendUrl}`);
  }

  public getProfileByUsername(username: string): Observable<Profile> {
    return this.http.get<Profile>(`${this.backendUrl}/${username}`);
  }

  /**
   * 
   * @param profileInfo New user profile (also update picture)
   * @returns new profile
   */
  public updateProfile(profileInfo: Profile): Observable<Profile> {
    return this.http.put<Profile>(`${this.backendUrl}`, profileInfo);
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

}
