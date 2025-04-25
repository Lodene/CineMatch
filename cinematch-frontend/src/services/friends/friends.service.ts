import { Injectable } from '@angular/core';
import { User } from '../../models/types/components/user/user.model';
import { HttpClient, HttpErrorResponse, HttpHeaders } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError, tap } from 'rxjs/operators';


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
    return this.http.get<User[]>(`${this.backendUrl}/friendship`)
      .pipe(
        catchError(this.handleError('Erreur lors de la récupération des amis'))
      );
  }

  /**
   * Récupère tous les utilisateurs
   * Correspond à GET /profile/all
   */
  public getAllUsers(): Observable<User[]> {
    return this.http.get<User[]>(`${this.backendUrl}/profile/all`)
      .pipe(
        catchError(this.handleError('Erreur lors de la récupération des utilisateurs'))
      );
  }

  /**
   * Supprime un ami
   * Correspond à DELETE /friendship/{username}
   */
  public deleteFriend(username: string): Observable<void> {
    return this.http.delete<void>(`${this.backendUrl}/friendship/${username}`)
      .pipe(
        catchError(this.handleError(`Erreur lors de la suppression de l'ami ${username}`))
      );
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
  public getReceivedRequests(): Observable<User[]> {
    return this.http.get<User[]>(`${this.backendUrl}/friend-requests`)
  }

  /**
   * Accepte une demande d'ami
   * Correspond à POST /friend-requests/accept/{requestId}
   */
  public acceptRequest(requestId: number): Observable<void> {
    return this.http.post<void>(`${this.backendUrl}/friend-requests/accept/${requestId}`, null)
      .pipe(
        catchError(this.handleError(`Erreur lors de l'acceptation de la demande d'ami ${requestId}`))
      );
  }

  /**
   * Refuse une demande d'ami
   * Correspond à POST /friend-requests/decline/{requestId}
   */
  public declineRequest(requestId: number): Observable<void> {
    return this.http.post<void>(`${this.backendUrl}/friend-requests/decline/${requestId}`, null)
      .pipe(
        catchError(this.handleError(`Erreur lors du refus de la demande d'ami ${requestId}`))
      );
  }

  // Gestion des erreurs HTTP
  private handleError(operation: string) {
    return (error: HttpErrorResponse): Observable<never> => {
      console.error(`${operation}:`, error);
      
      // Informations détaillées sur l'erreur pour le débogage
      console.error('Détails de l\'erreur:', {
        status: error.status,
        statusText: error.statusText,
        url: error.url,
        message: error.message,
        error: error.error
      });
      
      // Message d'erreur adapté en fonction du code HTTP
      let errorMessage = '';
      if (error.status === 400) {
        errorMessage = `Requête incorrecte: ${error.error?.message || 'Format de données invalide'}`;
      } else if (error.status === 403) {
        errorMessage = 'Vous n\'êtes pas autorisé à effectuer cette action';
      } else if (error.status === 404) {
        errorMessage = 'Ressource introuvable (utilisateur inexistant)';
      } else if (error.status === 409) {
        errorMessage = 'Cette action n\'est pas possible (conflit)';
      } else {
        errorMessage = `${operation}: ${error.message}`;
      }
      
      return throwError(() => new Error(errorMessage));
    };
  }

}
