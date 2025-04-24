import { CommonModule } from '@angular/common';
import { Component, inject, Input, OnInit } from '@angular/core';
import { MatCardModule } from '@angular/material/card';
import { MatDividerModule } from '@angular/material/divider';
import { MatIconModule } from '@angular/material/icon';
import { HistoryEntry } from '../../../models/history/history-entry';
import { TranslateService } from '@ngx-translate/core';
import { MovieActionType } from '../../../models/history/movie-action-type';
import { timeAgoValue } from '../../../utils/timeUtils';
import { Router } from '@angular/router';

@Component({
  selector: 'app-history-entry',
  standalone: true,
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

  private router = inject(Router);
  translateService = inject(TranslateService);

  @Input() historyEntry: HistoryEntry;

  movieActionType = MovieActionType;
  iconValue = "";
  textBefore = "";
  textAfter = "";
  timestampValue = "";

  ngOnInit(): void {
    this.initValue();
    this.timestampValue = this.formatTimeStamp(this.historyEntry.timestamp);
  }

  private initValue() {
    const title = this.historyEntry.movieTitle;

    let translated = "";
    switch(this.historyEntry.actionType) {
      case MovieActionType.LIKE:
        this.iconValue = 'favorite';
        translated = this.translateService.instant('app.common-component.profile.sections.activity.like', { film: '%%FILM%%' });
        break;
      case MovieActionType.UNLIKE:
        this.iconValue = 'favorite_border';
        translated = this.translateService.instant('app.common-component.profile.sections.activity.unlike', { film: '%%FILM%%' });
        break;
      case MovieActionType.WATCHLIST_ADD:
        this.iconValue = 'playlist_add';
        translated = this.translateService.instant('app.common-component.profile.sections.activity.watchlist-add', { film: '%%FILM%%' });
        break;
      case MovieActionType.WATCHLIST_REMOVE:
        this.iconValue = 'playlist_remove';
        translated = this.translateService.instant('app.common-component.profile.sections.activity.watchlist-remove', { film: '%%FILM%%' });
        break;
      case MovieActionType.REVIEW_ADD:
        this.iconValue = 'add_comment';
        translated = this.translateService.instant('app.common-component.profile.sections.activity.review-add', { film: '%%FILM%%' });
        break;
      case MovieActionType.REVIEW_REMOVE:
        this.iconValue = 'chat_error';
        translated = this.translateService.instant('app.common-component.profile.sections.activity.review-remove', { film: '%%FILM%%' });
        break;
      case MovieActionType.REVIEW_EDIT:
        this.iconValue = 'edit';
        translated = this.translateService.instant('app.common-component.profile.sections.activity.review-edit', { film: '%%FILM%%' });
        break;
      case MovieActionType.WATCHED:
        this.iconValue = 'visibility';
        translated = this.translateService.instant('app.common-component.profile.sections.activity.watch', { film: '%%FILM%%' });
        break;
      case MovieActionType.UNWATCHED:
        this.iconValue = 'visibility_off';
        translated = this.translateService.instant('app.common-component.profile.sections.activity.unwatch', { film: '%%FILM%%' });
        break;
    }

    const [before, after] = translated.split('%%FILM%%');
    this.textBefore = before;
    this.textAfter = after;
  }

  goToMovie(movieId: number) {
    this.router.navigate(['/movie', movieId]);
  }

  formatTimeStamp(timeStamp: string): string {
    const result = timeAgoValue(timeStamp);
    switch(result.unit) {
      case 'minutes':
        return this.translateService.instant('app.common-component.profile.sections.activity.time.minutes', { time: result.value });
      case 'hours':
        return this.translateService.instant('app.common-component.profile.sections.activity.time.hour', { time: result.value });
      case 'days':
        return this.translateService.instant('app.common-component.profile.sections.activity.time.day', { time: result.value });
      case 'weeks':
        return this.translateService.instant('app.common-component.profile.sections.activity.time.week', { time: result.value });
      case 'months':
        return this.translateService.instant('app.common-component.profile.sections.activity.time.month', { time: result.value });
      case 'years':
        return this.translateService.instant('app.common-component.profile.sections.activity.time.year', { time: result.value });
      default:
        return '';
    }
  }
}
