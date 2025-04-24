import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { MatCardModule } from '@angular/material/card';
import { TranslatePipe } from '@ngx-translate/core';

@Component({
  selector: 'app-friend-menu',
  imports: [
    CommonModule,
    TranslatePipe,
    MatCardModule
  ],
  templateUrl: './friend-menu.component.html',
  styleUrl: './friend-menu.component.scss'
})
/**
 * Used to navigate bewteend friendlist, search friend & manage friendRequest
 */
export class FriendMenuComponent {

}
