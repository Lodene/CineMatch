import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatIconModule } from '@angular/material/icon';
import { MatTabsModule } from '@angular/material/tabs';
import { MatCardModule } from '@angular/material/card';
import { MatDividerModule } from '@angular/material/divider';
import { MatButtonModule } from '@angular/material/button';
import { RouterModule } from '@angular/router';
import { TranslatePipe } from '@ngx-translate/core';
import { User } from '../../models/types/components/user/user.model';
import { Movie } from '../../models/movie';
import { FeaturedFilmComponent } from '../featured-film/featured-film.component';



@Component({
  selector: 'app-profil',
  imports: [
    CommonModule,
    RouterModule,
    MatIconModule,
    MatTabsModule,       
    MatCardModule,       
    MatDividerModule,    
    MatButtonModule,     
    TranslatePipe
  ],  
  templateUrl: './profil.component.html',
  styleUrls: ['./profil.component.scss']

})
export class ProfilComponent {
  favoriteMovies: Movie[] = [];

  userProfile: User = {
    id: 1,
    name: 'John Doe',
    email: 'john.doe@example.com',
    bio: 'Movie lover, tech enthusiast and popcorn addict.',
    avatarUrl: 'https://i.pravatar.cc/300',
    birthDate: '1990-01-01',
    location: 'Paris, France',
    favoriteGenres: ['Drama', 'Action']
  }
  
  ngOnInit(): void {
    this.favoriteMovies.push(new Movie());
    this.favoriteMovies.push(new Movie());
    this.favoriteMovies.push(new Movie());
    
  }
}
