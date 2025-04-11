import { Component } from '@angular/core';
import { MovieCardComponentHorizontal } from "../common-component/movie-card-horizontal/movie-card-horizontal.component";
import { Movie } from '../../models/movie';

@Component({
  selector: 'app-watch-list',
  imports: [MovieCardComponentHorizontal],
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
