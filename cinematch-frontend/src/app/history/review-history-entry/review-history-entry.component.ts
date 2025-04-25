import { Component, Input, OnInit } from '@angular/core';
import { Review } from '../../../models/review';
import { MovieService } from '../../../services/movie/movie.service';
import { Router } from '@angular/router';
import { MovieConsultation } from '../../../models/movieConsultation';
import {MovieImageUtils} from '../../../utils/movieImageUtils';
import { DatePipe} from '@angular/common';
import { MatIconModule } from '@angular/material/icon';

@Component({
  selector: 'app-review-history-entry',
  templateUrl: './review-history-entry.component.html',
  styleUrls: ['./review-history-entry.component.scss'],
  imports: [
    DatePipe,
    MatIconModule
  ],
  providers: [DatePipe]
})
export class ReviewHistoryEntryComponent implements OnInit {
  @Input() review: Review;
  movieTitle: string;
  moviePosterUrl: string;

  constructor(private movieService: MovieService, private router: Router, private datePipe: DatePipe) {}

  ngOnInit(): void {
    this.getMovieDetails(this.review.idMovie);
  }

  // Récupère les détails du film via l'ID du film
  getMovieDetails(movieId: number): void {
    this.movieService.getMovieById(movieId).subscribe({
      next: (movieConsultation: MovieConsultation) => {
        this.movieTitle = movieConsultation.movie.title;
        this.moviePosterUrl = MovieImageUtils.constructUrl(movieConsultation.movie.posterPath);
      },
      error: (err) => {
        console.error('Erreur de récupération des détails du film:', err);
      },
    });
  }

  goToMovie(movieId: number): void {
    this.router.navigate(['/movie', movieId]);
  }
}
