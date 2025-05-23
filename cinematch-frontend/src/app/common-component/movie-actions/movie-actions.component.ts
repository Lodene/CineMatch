import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, Input, OnChanges, OnInit, output } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { TooltipPosition, MatTooltipModule } from '@angular/material/tooltip';
import { TranslatePipe, TranslateService } from '@ngx-translate/core';
import { SnackbarService } from '../../../services/snackbar.service';
import { Review } from '../../../models/review';

@Component({
  selector: 'app-movie-actions',
  imports: [MatIconModule,
    MatButtonModule,
    MatTooltipModule,
    CommonModule
  ],
  templateUrl: './movie-actions.component.html',
  styleUrl: './movie-actions.component.scss'
})

export class MovieActionsComponent implements OnInit, OnChanges {


  likeActionEvent = output<number>();
  watchlistActionEvent = output<number>();
  reviewActionEvent = output<number>();
  watchedActionEvent = output<number>();

  likeToolTip = "";
  watchListToolTip = "";
  reviewToolTip = "";
  watchToolTip = "";
  /**
   * mat-icon value (eg. 'home')
  */
  favoriteValue: string = "favorite_border"
  /**
   * mat-icon value (eg. 'home')
  */
  watchListIconValue: string = "playlist_add";
  /**
   * mat-icon value (eg. 'home')
  */
  addReviewIconValue: string = "add_comment";

  watchedIconValue: string = "visibility";

  @Input() movieId: number;
  @Input() disabled: boolean

  @Input() isLiked: boolean;
  @Input() isInWatchList: boolean;
  @Input() isReviewAdded: boolean;
  @Input() isWatched: boolean;

  @Input() showWatchList: boolean;
  @Input() showAddReview: boolean;
  @Input() showLike: boolean;
  @Input() showWatched: boolean;

  constructor(private translateService: TranslateService) { }

  ngOnInit(): void {
    this.setToolTips();
    this.setIcons();
  }

  ngOnChanges(): void {
    this.setIcons();
    this.setToolTips();
  }

  handleLike(): void {
    if (!this.disabled)
      this.likeActionEvent.emit(this.movieId);
  }

  handleWatchListAction(): void {
    if (!this.disabled)
      this.watchlistActionEvent.emit(this.movieId);
  }

  handleReviewAction(): void {
    if (!this.disabled)
      this.reviewActionEvent.emit(this.movieId);
  }

  handleWatchedAction(): void {
    if (!this.disabled)
      this.watchedActionEvent.emit(this.movieId);
  }

  /**
  * Format tooltip depending on user connection and movie state (liked, in watchlist)
  */
  private setToolTips(): void {
    this.likeToolTip = this.disabled ? this.translateService.instant('app.common-component.movie-actions.tooltip.need-connection') :
      this.isLiked ? this.translateService.instant('app.common-component.movie-actions.tooltip.unlike') :
        this.translateService.instant('app.common-component.movie-actions.tooltip.like');
    this.watchListToolTip = this.disabled ? this.translateService.instant('app.common-component.movie-actions.tooltip.need-connection') :
      this.isInWatchList ? this.translateService.instant('app.common-component.movie-actions.tooltip.remove-from-watchlist') :
        this.translateService.instant('app.common-component.movie-actions.tooltip.add-to-watchlist');

    this.reviewToolTip = this.disabled ? this.translateService.instant('app.common-component.movie-actions.tooltip.need-connection') :
      this.isReviewAdded ? this.translateService.instant('app.common-component.movie-actions.tooltip.edit-review') :
        this.translateService.instant('app.common-component.movie-actions.tooltip.add-review');

    this.watchToolTip = this.disabled ? this.translateService.instant('app.common-component.movie-actions.tooltip.need-connection') :
      this.isWatched ? this.translateService.instant('app.common-component.movie-actions.tooltip.remove-from-watched') :
        this.translateService.instant('app.common-component.movie-actions.tooltip.add-to-watched');


  }

  /**
   * Set Icons based on movie state
   */
  private setIcons(): void {
    this.favoriteValue = this.disabled || !this.isLiked ? "favorite_border" : "favorite";
    this.watchListIconValue = this.disabled || !this.isInWatchList ? "playlist_add" : "playlist_remove";
    this.addReviewIconValue  = this.disabled || !this.isReviewAdded ? "add_comment" : "edit";
    this.watchedIconValue = this.disabled || !this.isWatched ? "visibility" : "visibility_off";
  }



}
