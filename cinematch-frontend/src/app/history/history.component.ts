import { CommonModule } from '@angular/common';
import { Component, inject, Input } from '@angular/core';
import { MatCardModule } from '@angular/material/card';
import { MatDividerModule } from '@angular/material/divider';
import { MatIconModule } from '@angular/material/icon';
import { MatTabsModule } from '@angular/material/tabs';
import { TranslateModule } from '@ngx-translate/core';
import { HistoryService } from '../../services/profile/history.service';
import { User } from '../../models/types/components/user/user.model';
import { firstValueFrom } from 'rxjs';
import { MovieService } from '../../services/movie/movie.service';
import { PaginatedHistoryResponse } from '../../models/history/paginated-history-response';
import { MovieActionType } from '../../models/history/movie-action-type';
import { ProfileService } from '../../services/profile/profile.service';
import { HistoryEntryComponent } from "./history-entry/history-entry.component";
import { DiaryService } from '../../services/diary/diary.service';
import { MovieConsultation } from '../../models/movieConsultation';
import { Movie } from '../../models/movie';
import { MovieHistoryEntryComponent} from './movie-history-entry/movie-history-entry.component';
import { ReviewHistoryEntryComponent} from './review-history-entry/review-history-entry.component';
import { LovedMovieService } from '../../services/loved-movie/loved-movie.service';
import { WatchlistService} from '../../services/watchlist/watchlist.service';
import { ReviewService} from '../../services/review/review.service';
import { Review } from '../../models/review';

@Component({
  selector: 'app-history',
  imports: [
    MatTabsModule,
    CommonModule,
    MatCardModule,
    MatIconModule,
    MatDividerModule,
    TranslateModule,
    HistoryEntryComponent,
    MovieHistoryEntryComponent,
    ReviewHistoryEntryComponent,
],
  templateUrl: './history.component.html',
  styleUrl: './history.component.scss'
})
export class HistoryComponent {

  @Input() username: string;

  profileService = inject(ProfileService);

  constructor(
    private _historyService: HistoryService,
    private _movieService: MovieService,
    private _diaryService: DiaryService,
    private _lovedService: LovedMovieService,
    private _watchlistService: WatchlistService,
    private _reviewService: ReviewService
  ) { }

  history : PaginatedHistoryResponse;
  moviesWatched: MovieConsultation[] = [];
  moviesLoved: MovieConsultation[] = [];
  moviesInWatchlist : MovieConsultation[] = [];
  reviews : Review[] = [];

  ngOnInit() {
    this._historyService.getUserMovieHistoryByUsername(this.username).subscribe({
      next: (result: PaginatedHistoryResponse) => {
        this.history = result;
        console.log(result);
      }, error: (err => {
        console.error(err);
      }),
    }).add(() => {

    });

    this._diaryService.getWatchedMoviesByUsername(this.username).subscribe((movies) => {
      this.moviesWatched = movies;
    });

    this._lovedService.getLovedMoviesByUsername(this.username).subscribe((movies) => {
      this.moviesLoved = movies;
    });

    this._watchlistService.getWatchListByUsername(this.username).subscribe((movies) => {
      this.moviesInWatchlist = movies
    });

    this._reviewService.getReviewByUsername(this.username).subscribe((reviews: Review[]) => {
      this.reviews = reviews;
    })

  }
}
