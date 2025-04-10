import { Component, OnInit } from '@angular/core';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatButtonModule } from '@angular/material/button';
import { MatMenuModule } from '@angular/material/menu';
import { MatIconModule } from '@angular/material/icon';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { RouterModule } from '@angular/router';
import { HeaderMenuItem } from '../../../models/types/components/header/header-components-types';
import { CommonModule } from '@angular/common';
import { TranslatePipe } from '@ngx-translate/core';

@Component({
  selector: 'app-header',
  imports: [
    CommonModule,
    MatToolbarModule,
    MatButtonModule,
    MatMenuModule,
    MatFormFieldModule, 
    MatInputModule, 
    MatIconModule,
    RouterModule,
    TranslatePipe],
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent implements OnInit{

  headerMenuItem = [
    { id: 1, shortLink: '/', icon: 'home', textKey: 'app.common-component.header.menu-item.home' },
    { id: 2, shortLink: '/recommendations', icon: 'thumb_up', textKey: 'app.common-component.header.menu-item.recommendations' },
    { id: 3, shortLink: '/watchlist', icon: 'list', textKey: 'app.common-component.header.menu-item.watchlist' },
    { id: 4, shortLink: '/diary', icon: 'menu_book', textKey: 'app.common-component.header.menu-item.diary' },
    { id: 5, shortLink: '/favorites', icon: 'favorite_border', textKey: 'app.common-component.header.menu-item.favorites' },
    { id: 6, shortLink: '/profile', icon: 'person', textKey: 'app.common-component.header.menu-item.profile' },
  ];
  

  ngOnInit(): void {


  }
}
