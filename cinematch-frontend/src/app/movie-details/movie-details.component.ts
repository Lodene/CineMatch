import { CommonModule, NgFor, NgIf } from '@angular/common';
import { Component, inject, signal, ViewEncapsulation } from '@angular/core';
import { MatIconModule } from '@angular/material/icon';
import { ActivatedRoute, Params } from '@angular/router';
import { catchError, finalize, firstValueFrom, of } from 'rxjs';
import { Movie } from '../../models/movie';
import { MovieService } from '../../services/movie/movie.service';
import { LoaderService } from '../../services/loader/loader.service';
import { ToastrService } from 'ngx-toastr';
import { TranslateDirective, TranslatePipe, TranslateService } from '@ngx-translate/core';
import { ListPersonComponent } from '../list-person/list-person.component';
import { MovieConsultation } from '../../models/movieConsultation';
import { MovieImageUtils } from '../../utils/movieImageUtils';
import { MovieActionsComponent } from '../common-component/movie-actions/movie-actions.component';
import { FavoriteMovieService } from '../../services/favorite-movie/favorite-movie.service';
import { AuthService } from '../../services/auth/auth.service';
import { error } from 'console';
import { WatchlistService } from '../../services/watchlist/watchlist.service';
import { MatTabsModule } from '@angular/material/tabs';
import { AboutMovieComponent } from './about-movie/about-movie.component';
import { ReviewCardComponent } from '../common-component/review-card/review-card.component';
import { SnackbarService } from '../../services/snackbar.service';
import { ReviewService } from '../../services/review/review.service';
import { DiaryService} from '../../services/diary/diary.service';
import { Review } from '../../models/review';
import { MatDialog } from '@angular/material/dialog';
import { AddReviewDialogComponent, AddReviewDialogData } from '../common-component/add-review-dialog/add-review-dialog.component';
import { ProfileService } from '../../services/profile/profile.service';
import { MovieCardComponent } from "../common-component/movie-card/movie-card.component";
import { MovieCardComponentHorizontal } from "../common-component/movie-card-horizontal/movie-card-horizontal.component";

@Component({
  selector: 'app-movie-details',
  imports: [
    NgIf,
    NgFor,
    MatIconModule,
    CommonModule,
    TranslatePipe,
    ListPersonComponent,
    MovieActionsComponent,
    ListPersonComponent,
    MatTabsModule,
    AboutMovieComponent,
    ReviewCardComponent,
    MovieCardComponent,
],
  encapsulation: ViewEncapsulation.None,
  templateUrl: './movie-details.component.html',
  styleUrl: './movie-details.component.scss'
})
export class MovieDetailsComponent {

  constructor(
    private movieService: MovieService,
    private route: ActivatedRoute,
    private loaderService: LoaderService,
    private toasterService: ToastrService,
    private favoriteMovieService: FavoriteMovieService,
    private reviewService: ReviewService,
    private watchlistService: WatchlistService,
    private diaryService: DiaryService,
    private authService: AuthService,
    private profileService: ProfileService,
    private snackbarService: SnackbarService,
    private translateService: TranslateService,
  ) { }

  idMovie: number;
  movie: Movie;
  isLogged = false;
  isLiked = false;
  isReviewAdded = false;
  isInWatchlist = false;
  isWatched = false;
  movieConsultation: MovieConsultation;
  reviews: Review[];
  username: string | null;
  
  similarMovies: Movie[] = [];
  
  // used for image
  backdropUrl: string;
  posterUrl: string



  async ngOnInit() {
    this.loaderService.show();
    this.isLogged = this.authService.isAuthenticated();
    this.username = await firstValueFrom(this.profileService.currentUsername)
    this.route.params.subscribe({
      next: (params: Params) => {
        this.idMovie = +params['id'];
        this.movieService.getMovieById(this.idMovie).subscribe(
          {
            next: (movieConsultation: MovieConsultation) => {
              this.movieConsultation = movieConsultation;
              this.movie = movieConsultation.movie;
              this.backdropUrl = MovieImageUtils.constructUrl(movieConsultation.movie.backdropPath);
              this.posterUrl = MovieImageUtils.constructUrl(movieConsultation.movie.posterPath);
              this.isInWatchlist = movieConsultation.inWatchlist;
              this.isLiked = movieConsultation.loved;
              this.reviews = movieConsultation.reviews;
              this.isWatched = movieConsultation.watched;
              this.isReviewAdded = movieConsultation.commented;

            },
            error: (error) => {
              if (error?.err) {
                this.toasterService.error(error.error.reason, error.error.error);
              } else {
                console.error(error);
              }
            }
          }).add(() => {
            this.loaderService.hide();
          });
        this.getSimilarMovie(this.idMovie);
      },
      error: (error) => {
        console.error(error);
      }
    });
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

  handleWatchAction($event: number) {
    this.diaryService.watchOrUnwatchMovie($event).subscribe({
      next: () => {
        // inversion
        this.isWatched = !this.isWatched;
        this.showWatchedSnackbar(this.isWatched);
      },
      error: (err) => {
        this.toasterService.error(err.error.reason, err.error.error);
      }
    });
  }


  watchTrailer(): void {
    if (!this.movie?.id) return;
    this.movieService.getTrailerUrl(this.movie.id).subscribe({
      next: (url: string) => {
        window.open(url, '_blank');
      },
      error: () => {
        this.snackbarService.show(
          this.translateService.instant('app.common-component.movie-details.trailer-not-found')
        );
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

  showWatchedSnackbar(action: boolean) {
    this.snackbarService.show(
      action ? this.translateService.instant('app.common-component.movie-actions.snackbar.add-to-watched') :
        this.translateService.instant('app.common-component.movie-actions.snackbar.remove-from-watched')
    );
  }


  readonly dialog = inject(MatDialog);

  handleReviewAction(movieId: number): void {
    const existingReview = this.movieConsultation.reviews.find(m => m.username === this.username);
    let review: Review;

    if (existingReview) {
      review = existingReview;
    } else {
      review = new Review();
      review.username = this.username as string;
      review.idMovie = movieId;
      review.movieTitle = this.movie.title;
      review.createdAt = new Date();
    }
    console.log(review);
    const dialogRef = this.dialog.open(AddReviewDialogComponent, {
      width: '70%',
      height: '60%',

      data: {
        review: review,
        description: review.description !== undefined &&
          review.description !== null ? review.description : '',
        // 0.5 to have a semi star at minimal
        note: review.note !== undefined && review.note !== null ? review.note : 0.5
      } as AddReviewDialogData,
    });

    dialogRef.afterClosed().subscribe(async result => {
      if (result === 'DELETE' && existingReview) {
        const index = this.movieConsultation.reviews.findIndex(r => r.id === review.id);
        if (index !== -1) {
          this.movieConsultation.reviews.splice(index, 1);
        }

        try {
          // Example of handling API call with firstValueFrom
          var deletedReview = await firstValueFrom(
            this.reviewService.deleteReview(review.id).pipe(
              catchError(error => {
                console.error('Error deleting review:', error);

                this.snackbarService.show(this.translateService.instant('app.common-component.movie-details.failed-to-delete'));
                // Return a fallback value to continue the observable chain
                return of(null);
              })
            )

          );
          if (deletedReview) {
            this.isReviewAdded = false;
            this.snackbarService.show(
              this.translateService.instant('app.common-component.movie-details.deleted-successfuly'));
          }
        } catch (deleteError) {
          console.error('Unexpected error during delete:', deleteError);
          this.snackbarService.show(
            this.translateService.instant('app.common-component.movie-details.failed-to-delete'));
          }

      }
      else if (result !== undefined) {
        console.log(result);
        // Update the review
        review.description = result.description;
        review.note = result.userRating;
        review.modifiedAt = new Date();
        console.log(review);
        try {
          if (!existingReview) {
            this.loaderService.show()
            this.reviewService.createReview(review).subscribe(
              {
                next: ((res: Review) => {
                  this.isReviewAdded = true;
                  this.movieConsultation.reviews.push(res);
                  this.snackbarService.show(
                    this.translateService.instant('app.common-component.movie-details.added-successfuly'));
                }),
                error: ((err) => {
                  //err
                }),
              }
            ).add(() => {
              this.loaderService.hide()

            })
          } else {
            var updatedReview = await firstValueFrom(
              this.reviewService.updateReview(review.id, review).pipe(
                catchError(error => {
                  console.error('Error updating review:', error);
                  this.snackbarService.show(this.translateService.instant('app.common-component.movie-details.failed-to-update'));
                  return of(null);
                })
              )
            );
            if (updatedReview) {
              this.snackbarService.show(
                this.translateService.instant('app.common-component.movie-details.updated-successfuly'));
            }
          }
        } catch (saveError) {
          console.error('Unexpected error during save:', saveError);
          this.snackbarService.show(
            this.translateService.instant('app.common-component.movie-details.failed-to-create'))
        }
      }
    });
  }

  private getSimilarMovie(movieId: number): void {
    this.movieService.getSimilarMovie(movieId).subscribe({
      next: ((res: Movie[]) => {
        this.similarMovies = res;
      }),
      error: ((err:any) => {
        console.log(err);
      })
    }).add(() => {

    });
  }

}
