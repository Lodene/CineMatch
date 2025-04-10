import { Component } from '@angular/core';
import { MovieCardComponent } from '../common-component/movie-card/movie-card.component';
import { Movie } from '../../models/movie';
import { MovieCardComponentHorizontal } from '../common-component/movie-card-horizontal/movie-card-horizontal.component';
import { NgFor } from '@angular/common';

@Component({
  selector: 'app-home',
  imports: [
    MovieCardComponent,
    NgFor
  ],
  templateUrl: './home.component.html',
  styleUrl: './home.component.scss'
})
export class HomeComponent {
  movies: Movie[] = [];

  ngOnInit() {
    this.movies.push(new Movie())
    this.movies.push(new Movie())
    this.movies.push(new Movie())
    this.movies.push(new Movie())
  }
}
