import { Component, Input, OnChanges, OnInit, SimpleChanges } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { RouterModule } from '@angular/router';
import { Movie } from '../../models/movie';
import { MovieImageUtils } from '../../utils/movieImageUtils';
import { TranslatePipe } from '@ngx-translate/core';
import { MovieService } from '../../services/movie/movie.service'; // ✅ Import du service

@Component({
  selector: 'app-featured-film',
  standalone: true,
  imports: [CommonModule, MatButtonModule, MatIconModule, RouterModule, TranslatePipe],
  templateUrl: './featured-film.component.html',
  styleUrl: './featured-film.component.scss'
})
export class FeaturedFilmComponent implements OnInit, OnChanges {

  @Input() movie: Movie;
  backdropPath: string;

  constructor(private movieService: MovieService) {} // ✅ Injection du service

  ngOnInit(): void {
    if (!!this.movie) {
      this.backdropPath = MovieImageUtils.constructUrl(this.movie.backdropPath);
    }
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (!!this.movie) {
      this.backdropPath = MovieImageUtils.constructUrl(this.movie.backdropPath);
    }
  }

  // ✅ Nouvelle méthode pour ouvrir la bande-annonce
  watchTrailer(): void {
    if (!this.movie?.id) return;
    this.movieService.getTrailerUrl(this.movie.id).subscribe(url => {
      window.open(url, '_blank');
    }, () => {
      alert('Aucune bande-annonce disponible.');
    });
  }
}
