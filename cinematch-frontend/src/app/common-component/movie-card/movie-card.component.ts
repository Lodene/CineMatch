import { Component, Input, OnInit } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { Movie } from '../../../models/movie';
import { MatIconModule } from '@angular/material/icon';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { MovieImageUtils } from '../../../utils/movieImageUtils';
import { TranslatePipe } from '@ngx-translate/core';

@Component({
  selector: 'movie-card',
  imports: [
    MatIconModule,
    MatCardModule,
    MatButtonModule,
    CommonModule,
    TranslatePipe
  ],
  templateUrl: './movie-card.component.html',
  styleUrl: './movie-card.component.scss',
  standalone: true
})
export class MovieCardComponent implements OnInit {
  @Input() movie: Movie;
  posterPath: string;
  constructor(
    private router: Router
  ) { }

  ngOnInit(): void {
    this.posterPath = MovieImageUtils.constructUrl(this.movie.posterPath);
  }

  goToMovie(movie: Movie) {
    // console.log(movie, ['/movie', movie.id])
    this.router.navigate(['/movie', movie.id]);
  }


}
