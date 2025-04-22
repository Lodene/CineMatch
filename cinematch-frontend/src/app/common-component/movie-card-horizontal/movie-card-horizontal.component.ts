import { Component, Input, OnInit, output } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { Movie } from '../../../models/movie';
import { MatIconModule } from '@angular/material/icon';
import { CommonModule, NgFor } from '@angular/common';
import { MovieImageUtils } from '../../../utils/movieImageUtils';
import { TranslatePipe, TranslateService } from '@ngx-translate/core';
import { MovieActionsComponent } from "../movie-actions/movie-actions.component";
import { WatchlistService } from '../../../services/watchlist/watchlist.service';
import { FavoriteMovieService } from '../../../services/favorite-movie/favorite-movie.service';
import { ToastrService } from 'ngx-toastr';
import { SnackbarService } from '../../../services/snackbar.service';

@Component({
  selector: 'movie-card-horizontal',
  imports: [
    MatIconModule,
    MatCardModule,
    MatButtonModule,
    NgFor,
    CommonModule,
    TranslatePipe,
    MovieActionsComponent
],
  templateUrl: './movie-card-horizontal.component.html',
  styleUrl: './movie-card-horizontal.component.scss',
  standalone: true
})
export class MovieCardComponentHorizontal implements OnInit {

  @Input() movie: Movie;
  @Input() showWatchList: boolean;
  @Input() showLike: boolean;
  @Input() isLoved: boolean
  @Input() isInWatchlist: boolean;
  /**
   * output movie Id for deletion
   */
  deleteMovie = output<number>()

  posterPath: string;

  constructor(private watchlistService: WatchlistService,
    private favoriteMovieService: FavoriteMovieService,
    private toasterService: ToastrService,
    private snackbarService: SnackbarService,
    private translateService: TranslateService
  ) {
    
  }

  ngOnInit(): void {
    this.posterPath = MovieImageUtils.constructUrl(this.movie.posterPath);
  }

  handleWatchListAction($event: number) {
    this.watchlistService.addOrRemoveMovieFromWatchlist($event).subscribe({
      next: () => {
        this.isInWatchlist = !this.isInWatchlist;
        this.showWatchListSnackbar(this.isInWatchlist);
        if (this.isInWatchlist === false) {
          this.deleteMovie.emit(this.movie.id);
        }
      },
      error: (err) => {
        this.toasterService.error(err.error.reason, err.error.error);
      }
    })
  }

  handleLikeAction($event: number) {
    this.favoriteMovieService.likeOrUnlikeMovie($event).subscribe({
      next: () => {
        // inversion
        this.isLoved = !this.isLoved;
        this.showLovedSnackbar(this.isLoved);
        if (this.isLoved === false) {
          // suppression
          this.deleteMovie.emit(this.movie.id);
        }
      },
      error: (err) => {
        this.toasterService.error(err.error.reason, err.error.error);
      }
    });
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
