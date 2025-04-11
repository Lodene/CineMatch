import { MovieCardComponent } from '../common-component/movie-card/movie-card.component';
import { Movie } from '../../models/movie';
import { MovieCardComponentHorizontal } from '../common-component/movie-card-horizontal/movie-card-horizontal.component';
import { NgFor } from '@angular/common';

import { Component, OnInit } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import { Film } from '../../models/types/components/featured-film/film.model';
import { FeaturedFilmComponent } from '../featured-film/featured-film.component';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-home',
  imports: [CommonModule, FeaturedFilmComponent, MovieCardComponent],
  templateUrl: './home.component.html',
  styleUrl: './home.component.scss'
})
export class HomeComponent implements OnInit{
  
  film!: Film;
  movies: Movie[] = [];

  constructor(private translateService: TranslateService) {

  }

  ngOnInit(): void {
    this.movies.push(new Movie())
    this.movies.push(new Movie())
    this.movies.push(new Movie())
    this.movies.push(new Movie())
    this.film = this.getFilm();
  }

  private getFilm() : Film {
    return {
      id: 1,
      title: "The Shawshank Redemption",
      posterUrl: "https://image.tmdb.org/t/p/w500/q6y0Go1tsGEsmtFryDOJo3dEmqu.jpg",
      backdropUrl: "https://image.tmdb.org/t/p/original/kXfqcdQKsToO0OUXHcrrNCHDBzO.jpg",
      year: 1994,
      rating: 8.7,
      runtime: 142,
      genres: ["Drama", "Crime"],
      overview: "Framed in the 1940s for the double murder of his wife and her lover, upstanding banker Andy Dufresne begins a new life at the Shawshank prison, where he puts his accounting skills to work for an amoral warden. During his long stretch in prison, Dufresne comes to be admired by the other inmates -- including an older prisoner named Red -- for his integrity and unquenchable sense of hope.",
    };
  }
}

