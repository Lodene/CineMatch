import { CommonModule, NgIf } from '@angular/common';
import { Component } from '@angular/core';
import { MatIconModule } from '@angular/material/icon';
import { ActivatedRoute, Params } from '@angular/router';
import { finalize, firstValueFrom } from 'rxjs';
import { Movie } from '../../models/movie';
import { MovieService } from '../../services/movie/movie.service';
import { LoaderService } from '../../services/loader/loader.service';
import { ToastrService } from 'ngx-toastr';
import { TranslateDirective, TranslatePipe } from '@ngx-translate/core';
import { ListPersonComponent } from '../list-person/list-person.component';
import { MovieConsultation } from '../../models/movieConsultation';
import { MovieImageUtils } from '../../utils/movieImageUtils';
import { MovieActionsComponent } from '../common-component/movie-actions/movie-actions.component';
import { FavoriteMovieService } from '../../services/favorite-movie/favorite-movie.service';
import { AuthService } from '../../services/auth/auth.service';
import { error } from 'console';
import { WatchlistService } from '../../services/watchlist/watchlist.service';

@Component({
  selector: 'app-movie-details',
  imports: [
    NgIf,
    MatIconModule,
    CommonModule,
    TranslatePipe,
    ListPersonComponent,
    MovieActionsComponent
  ],
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
    private watchlistService: WatchlistService,
    private authService: AuthService
  ) { }

  idMovie: number;
  movie: Movie;
  isLogged = false;
  isLiked = false;
  isInWatchlist = false;

  // used for image
  backdropUrl: string;
  posterUrl: string


  ngOnInit() {
    this.loaderService.show();
    this.isLogged = this.authService.isAuthenticated();
    this.route.params.subscribe({
      next: (params: Params) => {
        this.idMovie = +params['id'];
        this.movieService.getMovieById(this.idMovie).subscribe(
          {
            next: (movieConsultation: MovieConsultation) => {
              this.movie = movieConsultation.movie;
              this.backdropUrl = MovieImageUtils.constructUrl(movieConsultation.movie.backdropPath);
              this.posterUrl = MovieImageUtils.constructUrl(movieConsultation.movie.posterPath);
              this.isInWatchlist = movieConsultation.inWatchlist;
              this.isLiked = movieConsultation.loved;
            },
            error: (error) => {
              if (error?.err){
                this.toasterService.error(error.error.reason, error.error.error);
              } else {
                console.error(error);
              }
            }
          }).add(() => {
            this.loaderService.hide();
          });
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
        console.log(this.isLiked);
      },
      error: (err) => {
        this.toasterService.error(err.error.reason, err.error.error);
      }
    });
  }
}
