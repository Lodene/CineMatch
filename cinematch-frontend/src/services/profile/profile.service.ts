import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { User } from '../../models/types/components/user/user.model';

@Injectable({
  providedIn: 'root'
})
export class ProfileService {
  backendUrl: string = 'http://localhost:8081/profile';

  public usernameSubject: BehaviorSubject<string | null> = new BehaviorSubject<string | null>(null);

  constructor(private http: HttpClient) {}

  public getProfil(): Observable<User> {
    return this.http.get<User>(`${this.backendUrl}`);
  }

  public getProfileByUsername(username: string): Observable<User> {
    return this.http.get<User>(`${this.backendUrl}/${username}`);
  }

  public updateProfile(profileInfo: User): Observable<User> {
    return this.http.put<User>(`${this.backendUrl}`, profileInfo);
  }

  /**
   * Envoie une image de profil en tant que fichier
   * @param formData FormData contenant l'image sous clé "file"
   */
  public updatePicture(formData: FormData): Observable<any> {
    return this.http.put(`${this.backendUrl}/picture/upload`, formData);
  }

  public deletePicture(): Observable<any> {
    return this.http.delete(`${this.backendUrl}/picture`);
  }

  public get currentUsername(): Observable<string | null> {
    return this.usernameSubject.asObservable();
  }
}
