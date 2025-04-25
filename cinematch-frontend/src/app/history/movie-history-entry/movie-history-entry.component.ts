import { Component, Input } from '@angular/core';
import { Movie } from '../../../models/movie'
import { Router } from '@angular/router';
import {MovieImageUtils} from '../../../utils/movieImageUtils';
import { MatIconModule } from '@angular/material/icon';
import { DatePipe} from '@angular/common';

@Component({
  selector: 'app-movie-history-entry',
  templateUrl: './movie-history-entry.component.html',
  styleUrls: ['./movie-history-entry.component.scss'],
  imports: [
    MatIconModule,
    DatePipe,
  ]
})
export class MovieHistoryEntryComponent {

  @Input() movie: Movie;

  posterPath: string;

  constructor(private router: Router) {}

  ngOnInit() {
    this.posterPath = MovieImageUtils.constructUrl(this.movie.posterPath);
  }

  goToMovie(movieId: number) {
    this.router.navigate(['/movie', movieId]);
  }
}
