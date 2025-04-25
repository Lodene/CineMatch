import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { MatCardModule } from '@angular/material/card';
import { RouterModule } from '@angular/router';
import { User } from '../../../models/types/components/user/user.model';
import { FriendsService } from '../../../services/friends/friends.service';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-friend-search',
  imports: [
    CommonModule,
    FormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatIconModule,
    MatSnackBarModule,
    MatCardModule,
    RouterModule
    //TranslatePipe
  ],
  templateUrl: './friend-search.component.html',
  styleUrl: './friend-search.component.scss'
})
export class FriendSearchComponent {
  searchTerm: string = '';
  searchResult?: User;
  friends: User[] = [];
  allUsers: User[] = []; 
  isLoading: boolean = false;
  error: string | null = null;
  sentRequests: string[] = []; // Pour suivre les demandes envoyées

  private toasterService = inject(ToastrService)

  constructor(
    private friendsService: FriendsService,
    private snackBar: MatSnackBar
  ) {}

  ngOnInit(): void {
    this.loadFriends();
    this.loadAllUsers(); 
  }

  loadFriends(): void {
    this.isLoading = true;
    this.friendsService.getFriends().subscribe({
      next: (friends) => {
        this.friends = friends;
        this.isLoading = false;
      },
      error: (err) => {
        console.error('Erreur de chargement des amis :', err);
        this.error = 'Impossible de charger la liste d\'amis';
        this.isLoading = false;
      }
    });
  }

  loadAllUsers(): void {
    this.isLoading = true;
    this.friendsService.getAllUsers().subscribe({
      next: (users) => {
        console.log('allUsers from API:', users);
        this.allUsers = Array.isArray(users) ? users : []; 
        this.isLoading = false;
      },
      error: (err) => {
        console.error('Erreur chargement allUsers :', err);
        this.error = 'Impossible de charger la liste des utilisateurs';
        this.isLoading = false;
      }
    });
  }

  search(): void {
    if (!this.searchTerm.trim()) {
      this.searchResult = undefined;
      return;
    }
    
    if (!Array.isArray(this.allUsers) || this.allUsers.length === 0) {
      if (this.allUsers.length === 0) {
        this.loadAllUsers();
        return;
      }
      console.warn('allUsers is not an array or is empty:', this.allUsers);
      return;
    }
  
    this.searchResult = this.allUsers.find(
      user => user.username.toLowerCase().includes(this.searchTerm.toLowerCase()) &&
              !this.friends.some(f => f.username === user.username) &&
              !this.sentRequests.includes(user.username) // Ne pas montrer les utilisateurs à qui on a déjà envoyé une demande
    );
  }
  
  addFriend(): void {
    if (this.searchResult) {
      this.isLoading = true;
      
      // Vérifier si l'utilisateur est déjà dans la liste des demandes envoyées
      if (this.sentRequests.includes(this.searchResult.username)) {
        this.snackBar.open('Demande déjà envoyée à cet utilisateur', 'Fermer', { duration: 3000 });
        this.isLoading = false;
        return;
      }
      
      // Vérifier si l'utilisateur est déjà un ami
      if (this.friends.some(friend => friend.username === this.searchResult?.username)) {
        this.snackBar.open('Cet utilisateur est déjà votre ami', 'Fermer', { duration: 3000 });
        this.isLoading = false;
        return;
      }
      
      this.friendsService.sendFriendRequest(this.searchResult.username).subscribe(
        {
          next: (res => {
          // Ajouter l'utilisateur à la liste des demandes envoyées
          this.sentRequests.push(this.searchResult!.username);
          
          // Afficher un message de succès
          this.snackBar.open(`Demande d'ami envoyée à ${this.searchResult?.username}`, 'Fermer', { duration: 3000 });
          
          // Réinitialiser la recherche
          this.searchResult = undefined;
          this.searchTerm = '';
          }), error: (err => {
            this.toasterService.error(err.error.reason, err.error.error);
            console.error(err);
          })
        }).add(() => {
          this.isLoading = false;

        });
    }
  }
}
