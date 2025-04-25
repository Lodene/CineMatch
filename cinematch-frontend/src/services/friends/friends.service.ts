import { Injectable } from '@angular/core';
import { User } from '../../models/types/components/user/user.model';
import { IncomingFriendRequestDto } from '../../models/types/components/user/user.model';
import { HttpClient, HttpErrorResponse, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { catchError, tap } from 'rxjs/operators';
import { map } from 'rxjs/operators';
@Injectable({
  providedIn: 'root'
})
export class FriendsService {
  private backendUrl = 'http://localhost:8081';

  constructor(private http: HttpClient) {}

  /**
   * Récupère la liste des amis de l'utilisateur connecté
   * Correspond à GET /friendship
   */
  public getFriends(): Observable<User[]> {
    return this.http.get<User[]>(`${this.backendUrl}/friendship`);
  }

  /**
   * Récupère tous les utilisateurs
   * Correspond à GET /profile/all
   */
  public getAllUsers(): Observable<User[]> {
    return this.http.get<User[]>(`${this.backendUrl}/profile/all`);
  }

  /**
   * Supprime un ami
   * Correspond à DELETE /friendship/{username}
   */
  public deleteFriend(username: string): Observable<void> {
    return this.http.delete<void>(`${this.backendUrl}/friendship/${username}`);
  }

  /**
    * Envoie une demande d'ami
    * Correspond à POST /friend-requests/{username}
    * Le backend n'attend aucun corps de requête, seulement le username en paramètre de chemin
    */
  public sendFriendRequest(username: string): Observable<void> {
    console.log(`Envoi d'une demande d'ami à ${username}`);
    
    // Utilisation de la méthode post sans body, avec les options comme troisième paramètre
    return this.http.post<void>(`${this.backendUrl}/friend-requests/${username}`, null, {});
  }
   
  /**
   * Récupère les demandes d'amis reçues
   * Correspond à GET /friend-requests
   */
  public getReceivedRequests(): Observable<IncomingFriendRequestDto[]> {
    return this.http.get<any[]>(`${this.backendUrl}/friend-requests`)
      .pipe(
        tap(data => console.log('Données brutes de l\'API:', data)),
        map((data: any) => {
          // Si l'API renvoie directement un tableau d'objets
          if (Array.isArray(data)) {
            return data.map((item: any) => this.transformToFriendRequestDto(item));
          }
          // Si l'API encapsule le tableau dans un objet
          else if (data && Array.isArray(data.content)) {
            return data.content.map((item: any) => this.transformToFriendRequestDto(item));
          }
          // Fallback
          return [];
        })
      );
  }
  /**
 * Transforme les données brutes de l'API en IncomingFriendRequestDto
 */
private transformToFriendRequestDto(data: any): IncomingFriendRequestDto {
  // Vérifier la structure des données
  console.log('Transformation de la donnée brute:', data);
  
  // Créer un objet User à partir des données
  let sender: User;
  
  // Format reçu : {requestId, username, profilPicture}
  if (data.requestId && data.username) {
    sender = new User({
      username: data.username || 'Utilisateur inconnu',
      name: data.username || 'Utilisateur inconnu', // Utiliser username comme name par défaut
      profilPicture: data.profilPicture
    });
    
    // Retourner un format compatible avec IncomingFriendRequestDto
    return {
      id: data.requestId, // Utilisez requestId comme id
      sender: sender,
      createdAt: data.createdAt || new Date().toISOString()
    };
  }
  
  // Autres cas possibles (au cas où)
  sender = new User({
    username: data.username || 'Utilisateur inconnu',
    name: data.username || 'Utilisateur inconnu'
  });
  
  // Retourner un objet conforme à l'interface
  return {
    id: data.id || data.requestId || 0, // Priorisez requestId si id n'existe pas
    sender: sender,
    createdAt: data.createdAt || new Date().toISOString()
  };
}
/**
   * Accepte une demande d'ami
   * Correspond à POST /friend-requests/accept/{requestId}
   */
  public acceptRequest(requestId: number): Observable<void> {
    return this.http.post<void>(`${this.backendUrl}/friend-requests/accept/${requestId}`, null, {});
  }

  /**
   * Refuse une demande d'ami
   * Correspond à POST /friend-requests/decline/{requestId}
   */
  public declineRequest(requestId: number): Observable<void> {
    return this.http.post<void>(`${this.backendUrl}/friend-requests/decline/${requestId}`, null, {});
  }
   
}