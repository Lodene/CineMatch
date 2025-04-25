import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MatTabsModule } from '@angular/material/tabs';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { User } from '../../../models/types/components/user/user.model';
import { FriendsService } from '../../../services/friends/friends.service';

@Component({
  selector: 'app-manage-friend-request',
  imports: [
    CommonModule,
    FormsModule,
    MatTabsModule,
    MatCardModule,
    MatButtonModule,
    MatIconModule,
    MatFormFieldModule,
    MatInputModule,
    MatSnackBarModule
  ],
  templateUrl: './manage-friend-request.component.html',
  styleUrl: './manage-friend-request.component.scss'
})
export class ManageFriendRequestComponent implements OnInit{
  searchResult?: User;
  friends: User[] = [];
  allUsers: User[] = []; 
  isSearchLoading: boolean = false;
  searchError: string | null = null;
  sentRequests: string[] = [];

  // Variables pour les demandes reçues
  receivedRequests: any[] = [];
  isRequestsLoading: boolean = false;
  isProcessing: boolean = false;
  requestsError: string | null = null;

  constructor(
    private friendsService: FriendsService,
    private snackBar: MatSnackBar
  ) {}

  ngOnInit(): void {
    this.loadFriends();
    this.loadReceivedRequests();
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

  acceptRequest(requestId: number): void {
    this.isProcessing = true;
    
    this.friendsService.acceptRequest(requestId).subscribe({
      next: () => {
        // Trouver la demande acceptée pour obtenir l'utilisateur associé
        const acceptedRequest = this.receivedRequests.find(req => req.id === requestId);
        if (acceptedRequest) {
          // Actualiser la liste d'amis pour inclure le nouvel ami
          this.loadFriends();
        }
        
        // Supprimer la demande de la liste
        this.receivedRequests = this.receivedRequests.filter(req => req.id !== requestId);
        this.snackBar.open('Demande d\'ami acceptée', 'Fermer', { duration: 3000 });
        this.isProcessing = false;
      },
      error: (err) => {
        console.error('Erreur lors de l\'acceptation de la demande:', err);
        this.snackBar.open('Impossible d\'accepter la demande', 'Fermer', { duration: 5000 });
        this.isProcessing = false;
      }
    });
  }

  declineRequest(requestId: number): void {
    this.isProcessing = true;
    
    this.friendsService.declineRequest(requestId).subscribe({
      next: () => {
        // Supprimer la demande de la liste
        this.receivedRequests = this.receivedRequests.filter(req => req.id !== requestId);
        this.snackBar.open('Demande d\'ami refusée', 'Fermer', { duration: 3000 });
        this.isProcessing = false;
      },
      error: (err) => {
        console.error('Erreur lors du refus de la demande:', err);
        this.snackBar.open('Impossible de refuser la demande', 'Fermer', { duration: 5000 });
        this.isProcessing = false;
      }
    });
  }
}
