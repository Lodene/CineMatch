import { Component, OnInit } from '@angular/core';
import { CommonModule, DatePipe } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MatTabsModule } from '@angular/material/tabs';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { User } from '../../../models/types/components/user/user.model';
import { FriendRequest, IncomingFriendRequestDto } from '../../../models/types/components/user/user.model';
import { FriendsService } from '../../../services/friends/friends.service';
import { finalize } from 'rxjs/operators';

@Component({
  selector: 'app-manage-friend-request',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    MatTabsModule,
    MatCardModule,
    MatButtonModule,
    MatIconModule,
    MatFormFieldModule,
    MatInputModule,
    MatSnackBarModule,
    MatProgressSpinnerModule,
    DatePipe
  ],
  templateUrl: './manage-friend-request.component.html',
  styleUrl: './manage-friend-request.component.scss'
})
export class ManageFriendRequestComponent implements OnInit {
  searchQuery: string = '';
  searchResult?: User;
  friends: User[] = [];
  allUsers: User[] = []; 
  isSearchLoading: boolean = false;
  searchError: string | null = null;
  sentRequests: string[] = [];
  // Set pour suivre les demandes en cours de traitement
  processingRequestIds: Set<number> = new Set();

  // Variables pour les demandes reçues
  receivedRequests: IncomingFriendRequestDto[] = [];
  isRequestsLoading: boolean = false;
  requestsError: string | null = null;

  constructor(
    private friendsService: FriendsService,
    private snackBar: MatSnackBar
  ) {}

  ngOnInit(): void {
    this.loadFriends();
    this.loadReceivedRequests();
  }

  // Méthode pour vérifier si une demande est en cours de traitement
  isRequestProcessing(requestId: number): boolean {
    return this.processingRequestIds.has(requestId);
  }

  // Méthode pour obtenir le chemin d'image de profil sécurisé
  getProfileImage(user: User): string {
    if (!user || !user.profilPicture || user.profilPicture === 'assets/default-avatar.png') {
      return 'https://via.placeholder.com/150'; // Fallback vers une image placeholder
    }
    return user.profilPicture;
  }

  // Méthodes pour la recherche d'amis
  loadFriends(): void {
    this.isSearchLoading = true;
    this.friendsService.getFriends().subscribe({
      next: (friends) => {
        this.friends = friends;
        this.isSearchLoading = false;
      },
      error: (err) => {
        console.error('Erreur de chargement des amis :', err);
        this.searchError = 'Impossible de charger la liste d\'amis';
        this.isSearchLoading = false;
      }
    });
  }

  // Méthode de recherche d'utilisateur
  searchUser(): void {
    if (!this.searchQuery || this.searchQuery.trim() === '') {
      this.searchError = 'Veuillez entrer un nom d\'utilisateur';
      return;
    }

    this.isSearchLoading = true;
    this.searchError = null;
    this.searchResult = undefined;

    // Chargement de tous les utilisateurs si pas déjà fait
    if (this.allUsers.length === 0) {
      this.friendsService.getAllUsers().subscribe({
        next: (users) => {
          this.allUsers = users;
          this.findUserInList();
        },
        error: (err) => {
          console.error('Erreur lors du chargement des utilisateurs:', err);
          this.searchError = 'Impossible de charger la liste des utilisateurs';
          this.isSearchLoading = false;
        }
      });
    } else {
      this.findUserInList();
    }
  }

  // Cherche l'utilisateur dans la liste chargée
  private findUserInList(): void {
    const user = this.allUsers.find(u => 
      u.username.toLowerCase() === this.searchQuery.toLowerCase()
    );

    if (user) {
      this.searchResult = user;
      this.searchError = null;
    } else {
      this.searchError = 'Aucun utilisateur trouvé avec ce nom';
    }

    this.isSearchLoading = false;
  }

  addFriend(): void {
    if (this.searchResult) {
      this.isSearchLoading = true;
      
      if (this.sentRequests.includes(this.searchResult.username)) {
        this.snackBar.open('Demande déjà envoyée à cet utilisateur', 'Fermer', { duration: 3000 });
        this.isSearchLoading = false;
        return;
      }
      
      if (this.friends.some(friend => friend.username === this.searchResult?.username)) {
        this.snackBar.open('Cet utilisateur est déjà votre ami', 'Fermer', { duration: 3000 });
        this.isSearchLoading = false;
        return;
      }
      
      this.friendsService.sendFriendRequest(this.searchResult.username).subscribe({
        next: () => {
          this.sentRequests.push(this.searchResult!.username);
          this.snackBar.open(`Demande d'ami envoyée à ${this.searchResult?.username}`, 'Fermer', { duration: 3000 });
          this.searchResult = undefined;
          this.searchQuery = '';
          this.isSearchLoading = false;
        },
        error: (err) => {
          console.error('Erreur lors de l\'envoi de la demande d\'ami :', err);
          
          if (err.status === 403) {
            this.searchError = 'Vous n\'êtes pas autorisé à envoyer cette demande d\'ami';
          } else if (err.status === 409) {
            this.searchError = 'Une demande d\'ami a déjà été envoyée à cet utilisateur';
          } else {
            this.searchError = 'Impossible d\'envoyer la demande d\'ami. Veuillez réessayer.';
          }
          
          this.snackBar.open(this.searchError, 'Fermer', { duration: 5000 });
          this.isSearchLoading = false;
        }
      });
    }
  }

  removeFriend(user: User): void {
    this.isSearchLoading = true;
    this.friendsService.deleteFriend(user.username).subscribe({
      next: () => {
        this.friends = this.friends.filter(f => f.username !== user.username);
        this.snackBar.open(`${user.username} a été retiré de votre liste d'amis`, 'Fermer', { duration: 3000 });
        this.isSearchLoading = false;
      },
      error: (err) => {
        console.error('Erreur de suppression :', err);
        this.searchError = 'Impossible de supprimer cet ami';
        this.snackBar.open(this.searchError, 'Fermer', { duration: 5000 });
        this.isSearchLoading = false;
      }
    });
  }

  // Méthodes pour les demandes reçues
  loadReceivedRequests(): void {
    this.isRequestsLoading = true;
    this.requestsError = null;
    
    this.friendsService.getReceivedRequests().subscribe({
      next: (requests) => {
        console.log('Demandes reçues:', requests);
        this.receivedRequests = requests;
        this.isRequestsLoading = false;
      },
      error: (err) => {
        console.error('Erreur lors du chargement des demandes d\'amis:', err);
        this.requestsError = 'Impossible de charger les demandes d\'amis';
        this.isRequestsLoading = false;
      }
    });
  }

  // Méthode pour accepter une demande d'amitié
  acceptRequest(requestId: number): void {
    // Marquer comme en cours de traitement
    this.processingRequestIds.add(requestId);
    
    this.friendsService.acceptRequest(requestId)
      .pipe(
        // Utiliser finalize pour s'assurer que le traitement est terminé dans tous les cas
        finalize(() => {
          // Recharger les données depuis le backend pour refléter l'état actuel
          this.loadReceivedRequests();
          this.loadFriends();
          // Fin du traitement
          this.processingRequestIds.delete(requestId);
        })
      )
      .subscribe({
        next: () => {
          this.snackBar.open('Demande d\'amitié acceptée', 'Fermer', { duration: 3000 });
        },
        error: (error) => {
          console.error('Erreur lors de l\'acceptation de la demande:', error);
          this.snackBar.open('Erreur lors de l\'acceptation de la demande', 'Fermer', { duration: 3000 });
        }
      });
  }

  // Méthode pour refuser une demande d'amitié
  declineRequest(requestId: number): void {
    // Marquer comme en cours de traitement
    this.processingRequestIds.add(requestId);
    
    this.friendsService.declineRequest(requestId)
      .pipe(
        // Utiliser finalize pour s'assurer que le traitement est terminé dans tous les cas
        finalize(() => {
          // Recharger les demandes depuis le backend
          this.loadReceivedRequests();
          // Fin du traitement
          this.processingRequestIds.delete(requestId);
        })
      )
      .subscribe({
        next: () => {
          this.snackBar.open('Demande d\'amitié refusée', 'Fermer', { duration: 3000 });
        },
        error: (error) => {
          console.error('Erreur lors du refus de la demande:', error);
          this.snackBar.open('Erreur lors du refus de la demande', 'Fermer', { duration: 3000 });
        }
      });
  }
}