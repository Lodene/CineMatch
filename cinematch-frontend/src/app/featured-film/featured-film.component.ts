import { Component, Input, OnChanges, OnInit, SimpleChanges } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { Router, RouterModule } from '@angular/router';
import { Movie } from '../../models/movie';
import { MovieImageUtils } from '../../utils/movieImageUtils';
import { TranslatePipe, TranslateService } from '@ngx-translate/core';
import { MovieService } from '../../services/movie/movie.service';
import { MovieActionsComponent } from "../common-component/movie-actions/movie-actions.component";
import { AuthService } from '../../services/auth/auth.service';
import { WatchlistService } from '../../services/watchlist/watchlist.service';
import { SnackbarService } from '../../services/snackbar.service';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-featured-film',
  standalone: true,
  imports: [CommonModule, MatButtonModule, MatIconModule, RouterModule, TranslatePipe, MovieActionsComponent],
  templateUrl: './featured-film.component.html',
  styleUrl: './featured-film.component.scss'
})
export class FeaturedFilmComponent implements OnInit, OnChanges {

  @Input() movie: Movie;
  backdropPath: string;
  isLogged = false;
  isInWatchlist = false;


  constructor(
    private movieService: MovieService,
    private router: Router,
    private authService: AuthService,
    private watchlistService: WatchlistService,
    private translateService: TranslateService,
    private snackbarService: SnackbarService,
    private toasterService: ToastrService,

  ) { } // Injection du service

  ngOnInit(): void {
    this.isLogged = this.authService.isAuthenticated();
    if (!!this.movie) {
      this.backdropPath = MovieImageUtils.constructUrl(this.movie.backdropPath);
    }
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (!!this.movie) {
      this.backdropPath = MovieImageUtils.constructUrl(this.movie.backdropPath);
    }
  }

  watchTrailer(): void {
    if (!this.movie?.id) return;
    this.movieService.getTrailerUrl(this.movie.id).subscribe(url => {
      window.open(url, '_blank');
    }, () => {
      alert('Aucune bande-annonce disponible.');
    });
  }

  goToMovie(id: number) {
    this.router.navigate(['/movie', id]);

  }

  addToWatchlist() {
    throw new Error('Method not implemented.');
  }

  handleWatchListAction($event: number): void {
    this.watchlistService.addOrRemoveMovieFromWatchlist($event).subscribe({
      next: () => {
        this.isInWatchlist = !this.isInWatchlist;
        this.showWatchListSnackbar(this.isInWatchlist);
      },
      error: (err) => {
        this.toasterService.error(err.error.reason, err.error.error);
      }
    })
  }

    /**
   *
   * @param action either removed or added (false = remove, true = added)
   */
    showWatchListSnackbar(action: boolean) {
      this.snackbarService.show(
        action ? this.translateService.instant('app.common-component.movie-actions.snackbar.add-watchlist') :
          this.translateService.instant('app.common-component.movie-actions.snackbar.remove-watchlist')
      );
    }
  

}
