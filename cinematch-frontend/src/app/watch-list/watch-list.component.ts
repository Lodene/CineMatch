import { Component } from '@angular/core';
import { MovieCardComponentHorizontal } from "../common-component/movie-card-horizontal/movie-card-horizontal.component";
import { Movie } from '../../models/movie';
import { NgFor } from '@angular/common';

@Component({
  selector: 'app-watch-list',
  imports: [MovieCardComponentHorizontal, NgFor],
  templateUrl: './watch-list.component.html',
  styleUrl: './watch-list.component.scss'
})
export class WatchListComponent {

  movies: Movie[] = [];

  ngOnInit(): void {
    this.movies.push(new Movie())
    this.movies.push(new Movie())
    this.movies.push(new Movie())
    this.movies.push(new Movie())
  }
}
