import { CommonModule } from '@angular/common';
import { Component, inject, Input, OnInit } from '@angular/core';
import { MatCardModule } from '@angular/material/card';
import { MatDividerModule } from '@angular/material/divider';
import { MatIconModule } from '@angular/material/icon';
import { HistoryEntry } from '../../../models/history/history-entry';
import { TranslatePipe, TranslateService } from '@ngx-translate/core';
import { MovieActionType } from '../../../models/history/movie-action-type';
import { timeAgoValue } from '../../../utils/timeUtils';

@Component({
  selector: 'app-history-entry',
  imports: [
    CommonModule,
    MatCardModule,
    MatIconModule,
    MatDividerModule,
  ],
  templateUrl: './history-entry.component.html',
  styleUrl: './history-entry.component.scss'
})


export class HistoryEntryComponent implements OnInit {

  
  @Input()
  historyEntry: HistoryEntry;
  translateService = inject(TranslateService);
  movieActionType = MovieActionType;
  iconValue = "";
  textValue = "";
  timestampValue = "";
  ngOnInit(): void {
    this.initValue();
    this.timestampValue = this.formatTimeStamp(this.historyEntry.timestamp);
  }

  private initValue() {
    switch(this.historyEntry.actionType) {
      case MovieActionType.LIKE:
        this.formatLike(this.historyEntry.movieTitle);
        break;
      case MovieActionType.UNLIKE:
        this.formatUnlike(this.historyEntry.movieTitle);
        break;
      case MovieActionType.WATCHLIST_ADD:
        this.formatAddWatchlist(this.historyEntry.movieTitle);
        break;
      case MovieActionType.WATCHLIST_REMOVE:
        this.formatRemoveWatchlist(this.historyEntry.movieTitle);
        break;
      case MovieActionType.REVIEW_ADD:
        this.formatAddReview(this.historyEntry.movieTitle);
        break;
      case MovieActionType.REVIEW_REMOVE:
        this.formatRemoveReview(this.historyEntry.movieTitle);
        break;
      case MovieActionType.REVIEW_EDIT:
        this.formatEditReviw(this.historyEntry.movieTitle);
        break;
      case MovieActionType.WATCHED:
        this.formatLike(this.historyEntry.movieTitle);
        // not implemented yet
        break;
      case MovieActionType.UNWATCHED:
        this.formatLike(this.historyEntry.movieTitle);
        // not implemented yet
        break;
    }
  }


  private formatLike(movieTitle: string) {
    this.iconValue = 'favorite';
    this.textValue = this.translateService.instant('app.common-component.profile.sections.activity.like', {film: movieTitle});

  }
  private formatUnlike(movieTitle: string) {
    this.iconValue = 'favorite_border';
    this.textValue = this.translateService.instant('app.common-component.profile.sections.activity.unlike', {film: movieTitle});
  }

  private formatAddWatchlist(movieTitle: string) {
    this.iconValue = 'playlist_add'
    this.textValue = this.translateService.instant('app.common-component.profile.sections.activity.watchlist-add', {film: movieTitle});
  }

  private formatRemoveWatchlist(movieTitle: string) {
    this.iconValue = 'playlist_remove'
    this.textValue = this.translateService.instant('app.common-component.profile.sections.activity.watchlist-remove', {film: movieTitle});
  }

  private formatAddReview(movieTitle: string) {
    this.iconValue = 'add_comment';
    this.textValue = this.translateService.instant('app.common-component.profile.sections.activity.review-add', {film: movieTitle});
  }

  private formatEditReviw(movieTitle: string) {
    this.iconValue = 'edit';
    this.textValue = this.translateService.instant('app.common-component.profile.sections.activity.review-edit', {film: movieTitle});
  }

  private formatRemoveReview(movieTitle: string) {
    this.iconValue = 'chat_error';
    this.textValue = this.translateService.instant('app.common-component.profile.sections.activity.review-remove', {film: movieTitle});
  }


  private formatWatched() {

  }
  private formatUnwatched() {

  }

  formatTimeStamp(timeStamp: string): string {
    const result = timeAgoValue(timeStamp);
    console.log(result);
    switch(result.unit) {
      case 'minutes':
        return this.translateService.instant('app.common-component.profile.sections.activity.time.minutes', {time: result.value});
      case 'hours':
        return this.translateService.instant('app.common-component.profile.sections.activity.time.hour', {time: result.value});
      case 'days':
        return this.translateService.instant('app.common-component.profile.sections.activity.time.day', {time: result.value});
      case 'weeks':
        return this.translateService.instant('app.common-component.profile.sections.activity.time.week', {time: result.value});
      case 'months':
        return this.translateService.instant('app.common-component.profile.sections.activity.time.month', {time: result.value});
      case 'years':
        return this.translateService.instant('app.common-component.profile.sections.activity.time.year', {time: result.value});
      default:
        return '';
    }
  }

}
