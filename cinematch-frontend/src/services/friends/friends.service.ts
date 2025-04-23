import { Injectable } from '@angular/core';
import { User } from '../../models/types/components/user/user.model';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';



@Injectable({
  providedIn: 'root'
})
export class FriendsService {
  private backendUrl = 'http://localhost:8081';

  constructor(private http: HttpClient) {}

  // Récupérer la liste d'amis de l'utilisateur connecté
  public getFriends(): Observable<User[]> {
    return this.http.get<User[]>(`${this.backendUrl}/friendship`);
  }

  public getAllUsers(): Observable<User[]> {
    return this.http.get<User[]>(`${this.backendUrl}/profile`); 
  }

  // Supprimer un ami
  public deleteFriend(username: string): Observable<void> {
    return this.http.delete<void>(`${this.backendUrl}/friendship/${username}`);
  }

  // Envoyer une demande d'ami
  public sendFriendRequest(username: string): Observable<void> {
    return this.http.post<void>(`${this.backendUrl}/friend-requests/${username}`, {});
  }

  // Récupérer les demandes d'amis reçues
  public getReceivedRequests(): Observable<any[]> {
    return this.http.get<any[]>(`${this.backendUrl}/friend-requests`);
  }

  // Accepter une demande d'ami
  public acceptRequest(requestId: number): Observable<void> {
    return this.http.post<void>(`${this.backendUrl}/friend-requests/accept/${requestId}`, {});
  }

  // Refuser une demande d'ami
  public declineRequest(requestId: number): Observable<void> {
    return this.http.post<void>(`${this.backendUrl}/friend-requests/decline/${requestId}`, {});
  }

}
