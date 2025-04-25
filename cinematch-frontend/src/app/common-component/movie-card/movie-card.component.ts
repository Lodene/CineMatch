import { Component, Input, OnInit } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { Movie } from '../../../models/movie';
import { MatIconModule } from '@angular/material/icon';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { MovieImageUtils } from '../../../utils/movieImageUtils';
import { TranslatePipe, TranslateService } from '@ngx-translate/core';
import { MovieActionsComponent } from "../movie-actions/movie-actions.component";
import { ToastrService } from 'ngx-toastr';
import { AuthService } from '../../../services/auth/auth.service';
import { SnackbarService } from '../../../services/snackbar.service';
import { WatchlistService } from '../../../services/watchlist/watchlist.service';
import { FavoriteMovieService } from '../../../services/favorite-movie/favorite-movie.service';

@Component({
  selector: 'movie-card',
  imports: [
    MatIconModule,
    MatCardModule,
    MatButtonModule,
    CommonModule,
    TranslatePipe,
    MovieActionsComponent
  ],
  templateUrl: './movie-card.component.html',
  styleUrl: './movie-card.component.scss',
  standalone: true
})
export class MovieCardComponent implements OnInit {
  @Input() movie: Movie;
  posterPath: string;
  isLogged = false;
  isInWatchlist = false;
  hideElement = true;
  isLiked = false;

  constructor(
    private router: Router,
    private authService: AuthService,
    private watchlistService: WatchlistService,
    private translateService: TranslateService,
    private snackbarService: SnackbarService,
    private toasterService: ToastrService,
    private favoriteMovieService: FavoriteMovieService,

  ) { }


  ngOnInit(): void {
    this.posterPath = MovieImageUtils.constructUrl(this.movie.posterPath);
    this.isLogged = this.authService.isAuthenticated();
  }

  goToMovie(movie: Movie) {
    // console.log(movie, ['/movie', movie.id])
    this.router.navigate(['/movie', movie.id]);
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

  /**
   *
   * @param action either removed or added (false = remove, true = added)
   */
  showLovedSnackbar(action: boolean) {
    this.snackbarService.show(
      action ? this.translateService.instant('app.common-component.movie-actions.snackbar.like') :
        this.translateService.instant('app.common-component.movie-actions.snackbar.unlike')
    );
  }

  handleLikeAction($event: number): void {
    this.favoriteMovieService.likeOrUnlikeMovie($event).subscribe({
      next: () => {
        // inversion
        this.isLiked = !this.isLiked;
        this.showLovedSnackbar(this.isLiked);
      },
      error: (err) => {
        this.toasterService.error(err.error.reason, err.error.error);
      }
    });
  }


}
