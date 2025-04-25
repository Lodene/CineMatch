import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatDividerModule } from '@angular/material/divider';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { User } from '../../../models/types/components/user/user.model';
import { FriendsService } from '../../../services/friends/friends.service';
import { RouterModule } from '@angular/router';


@Component({
  selector: 'app-friends-list',
  imports: [
    CommonModule,
    RouterModule,
    MatCardModule,
    MatButtonModule,
    MatIconModule,
    MatDividerModule,
    MatSnackBarModule
  ],
  templateUrl: './friends-list.component.html',
  styleUrl: './friends-list.component.scss'
})
export class FriendsListComponent implements OnInit{
  friends: User[] = [];
  isLoading: boolean = false;
  error: string | null = null;

  constructor(
    private friendsService: FriendsService,
    private snackBar: MatSnackBar
  ) {}

  ngOnInit(): void {
    this.loadFriends();
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

  removeFriend(friend: User): void {
    this.isLoading = true;
    this.friendsService.deleteFriend(friend.username).subscribe({
      next: () => {
        this.friends = this.friends.filter(f => f.username !== friend.username);
        this.snackBar.open(`${friend.name} a été retiré(e) de votre liste d'amis`, 'Fermer', { duration: 3000 });
        this.isLoading = false;
      },
      error: (err) => {
        console.error('Erreur lors de la suppression de l\'ami :', err);
        this.error = 'Impossible de supprimer cet ami';
        this.snackBar.open(this.error, 'Fermer', { duration: 5000 });
        this.isLoading = false;
      }
    });
  }
}
