import { Component, Input } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { Movie } from '../../../models/movie';
import { MatIconModule } from '@angular/material/icon';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'movie-card',
  imports: [
    MatIconModule,
    MatCardModule,
    MatButtonModule,
    CommonModule
  ],
  templateUrl: './movie-card.component.html',
  styleUrl: './movie-card.component.scss',
  standalone: true
})
export class MovieCardComponent {
  @Input() movie: Movie;
  constructor(
    private router: Router
  ) { }

  goToMovie(movie: Movie) {
    console.log(movie, ['/movie', movie.id])
    this.router.navigate(['/movie', movie.id]);
  }


}
