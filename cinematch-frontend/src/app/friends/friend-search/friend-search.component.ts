import { Component, inject, OnInit } from '@angular/core';
import { CommonModule, NgFor } from '@angular/common';
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
import { AuthService } from '../../../services/auth/auth.service';
import { ProfileService } from '../../../services/profile/profile.service';

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
  searchResult?: User[];
  friends: User[] = [];
  allUsers: User[] = []; 
  isLoading: boolean = false;
  error: string | null = null;
  sentRequests: string[] = []; // Pour suivre les demandes envoyées
  
  username: string;
  
  private toasterService = inject(ToastrService)
  private profileService = inject(ProfileService);
  constructor(
    private friendsService: FriendsService,
    private snackBar: MatSnackBar,
  ) {}

  ngOnInit(): void {
    this.profileService.currentUsername.subscribe({
      next: ((username : string | null) => {
        if (username !== null) {
          this.username = username as string;
          this.loadFriends();
          this.loadAllUsers(); 
        } else {
          console.error("username not found");
        }
      }),
      error: (err => {
        console.error("username not found")
      })
    })

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
        this.allUsers = Array.isArray(users) ? users.filter(user => user.username !== this.username) : []; 
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
  
    this.searchResult = this.allUsers.filter(
      user => user.username.toLowerCase().includes(this.searchTerm.toLowerCase()) &&
              !this.friends.some(f => f.username === user.username) &&
              !this.sentRequests.includes(user.username) // Ne pas montrer les utilisateurs à qui on a déjà envoyé une demande
    );
    console.log(this.searchResult);
  }
  
  addFriend(username: string): void {
    if (this.searchResult) {
      this.isLoading = true;
      
      // Vérifier si l'utilisateur est déjà dans la liste des demandes envoyées
      if (this.sentRequests.includes(username)) {
        this.snackBar.open('Demande déjà envoyée à cet utilisateur', 'Fermer', { duration: 3000 });
        this.isLoading = false;
        return;
      }
      
      // Vérifier si l'utilisateur est déjà un ami
      if (this.friends.some(friend => friend.username === username)) {
        this.snackBar.open('Cet utilisateur est déjà votre ami', 'Fermer', { duration: 3000 });
        this.isLoading = false;
        return;
      }
      
      this.friendsService.sendFriendRequest(username).subscribe(
        {
          next: (res => {
          // Ajouter l'utilisateur à la liste des demandes envoyées
          this.sentRequests.push(username);
          
          // Afficher un message de succès
          this.snackBar.open(`Demande d'ami envoyée à ${username}`, 'Fermer', { duration: 3000 });
          
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
