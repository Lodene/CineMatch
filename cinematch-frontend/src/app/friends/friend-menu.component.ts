import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Router } from '@angular/router';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';


@Component({
  selector: 'app-friend-menu',
  imports: [
    CommonModule,
    RouterModule,
    MatToolbarModule,
    MatButtonModule,
    MatIconModule
  ],
  templateUrl: './friend-menu.component.html',
  styleUrl: './friend-menu.component.scss'
})
/**
 * Used to navigate bewteend friendlist, search friend & manage friendRequest
 */
export class FriendMenuComponent {
  constructor(private router: Router) {}

  // Méthode pour vérifier si un chemin est actif
  isRouteActive(path: string): boolean {
    return this.router.url.includes(path);
  }
}
