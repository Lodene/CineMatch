import { Component, OnInit } from '@angular/core';
import { User } from '../../models/types/components/user/user.model';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { FormsModule } from '@angular/forms';
import { TranslatePipe } from '@ngx-translate/core';
import { CommonModule } from '@angular/common';
import { FriendsService } from '../../services/friends/friends.service';



@Component({
  selector: 'app-friends-list',
  imports: [
    CommonModule,
    FormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatIconModule
    //TranslatePipe
  ],
  templateUrl: './friends-list.component.html',
  styleUrl: './friends-list.component.scss'
})
export class FriendsListComponent{
  searchTerm: string = '';
  searchResult?: User;
  friends: User[] = [];
  allUsers: User[] = []; 

  constructor(private friendsService: FriendsService) {}

  ngOnInit(): void {
    this.loadFriends();
    // Optionnel : précharger tous les utilisateurs pour la recherche
    this.loadAllUsers();
  }

  loadFriends(): void {
    this.friendsService.getFriends().subscribe({
      next: (friends) => this.friends = friends,
      error: (err) => console.error('Erreur de chargement des amis :', err)
    });
  }

  loadAllUsers(): void {
    this.friendsService.getAllUsers().subscribe({
      next: (users) => {
        console.log('allUsers from API:', users);
        this.allUsers = Array.isArray(users) ? users : []; 
      },
      error: (err) => console.error('Erreur chargement allUsers :', err)
    });
  }

  search(): void {
    if (!Array.isArray(this.allUsers)) {
      console.warn('allUsers is not an array:', this.allUsers);
      return;
    }
  
    this.searchResult = this.allUsers.find(
      user => user.username.toLowerCase().includes(this.searchTerm.toLowerCase()) &&
              !this.friends.some(f => f.username === user.username)
    );
  }
  

  addFriend(): void {
    if (this.searchResult) {
      this.friendsService.sendFriendRequest(this.searchResult.username).subscribe({
        next: () => {
          this.friends.push(this.searchResult!);
          this.searchResult = undefined;
          this.searchTerm = '';
        },
        error: (err) => console.error('Erreur lors de l’ajout :', err)
      });
    }
  }

  removeFriend(user: User): void {
    this.friendsService.deleteFriend(user.username).subscribe({
      next: () => {
        this.friends = this.friends.filter(f => f.username !== user.username);
      },
      error: (err) => console.error('Erreur de suppression :', err)
    });
  }
}
