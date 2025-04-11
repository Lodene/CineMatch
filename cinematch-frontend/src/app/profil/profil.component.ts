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
    TranslatePipe,
    FeaturedFilmComponent
  ],  
  templateUrl: './profil.component.html',
  styleUrls: ['./profil.component.scss']

})
export class ProfilComponent {
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
  
  favoriteMovies: Movie[] = [
    {
      id: 1,
      title: 'The Shawshank Redemption',
      description: 'Framed in the 1940s for the double murder of his wife and her lover, upstanding banker Andy Dufresne begins a new life at the Shawshank prison, where he puts his accounting skills to work for an amoral warden. During his long stretch in prison, Dufresne comes to be admired by the other inmates -- including an older prisoner named Red -- for his integrity and unquenchable sense of hope.',
      releaseDate: 1994,
      poster: 'https://image.tmdb.org/t/p/w500/q6y0Go1tsGEsmtFryDOJo3dEmqu.jpg',
      genre: ['Drama', 'Crime'],
      rating: 9.99,
      duration: 142
    },
    {
      id: 2,
      title: 'Inception',
      description: 'A thief who steals corporate secrets through dream-sharing technology...',
      releaseDate: 2010,
      poster: 'https://image.tmdb.org/t/p/w500/q6y0Go1tsGEsmtFryDOJo3dEmqu.jpg',
      genre: ['Sci-Fi', 'Action'],
      rating: 8.8,
      duration: 148
    }
  ];
}
