import { MovieCardComponent } from '../common-component/movie-card/movie-card.component';
import { Movie } from '../../models/movie';
import { MovieCardComponentHorizontal } from '../common-component/movie-card-horizontal/movie-card-horizontal.component';
import { NgFor } from '@angular/common';

import { Component, OnInit } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import { FeaturedFilmComponent } from '../featured-film/featured-film.component';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-home',
  imports: [CommonModule, FeaturedFilmComponent, MovieCardComponent],
  templateUrl: './home.component.html',
  styleUrl: './home.component.scss'
})
export class HomeComponent implements OnInit{
  
  film: Movie;
  movies: Movie[] = [];

  constructor(private translateService: TranslateService) {

  }

  ngOnInit(): void {
    this.movies.push(new Movie())
    this.movies.push(new Movie())
    this.movies.push(new Movie())
    this.movies.push(new Movie())
    this.film = new Movie();
  }

}

