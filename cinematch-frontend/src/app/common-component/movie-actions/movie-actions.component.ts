import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, Input, OnChanges, OnInit, output } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import {TooltipPosition, MatTooltipModule} from '@angular/material/tooltip';
import { TranslatePipe, TranslateService } from '@ngx-translate/core';
import { SnackbarService } from '../../../services/snackbar.service';
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

export class MovieActionsComponent implements OnInit, OnChanges
{

  
  likeActionEvent = output<number>();
  watchlistActionEvent = output<number>();
  
  likeToolTip = "";
  watchListToolTip = "";
  /**
   * mat-icon value (eg. 'home')
  */ 
  favoriteValue: string = "favorite_border"
  /**
   * mat-icon value (eg. 'home')
  */ 
  watchListIconValue: string = "playlist_add";

  @Input() movieId: number;
  @Input() disabled: boolean

  @Input() isLiked: boolean;
  @Input() isInWatchList: boolean;

  @Input() showWatchList: boolean
  @Input() showLike: boolean;

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
  }

  /**
   * Set Icons based on movie state
   */
  private setIcons(): void {
    this.favoriteValue = this.disabled || !this.isLiked ? "favorite_border": "favorite";
    this.watchListIconValue = this.disabled || !this.isInWatchList ? "playlist_add" : "playlist_remove";
  }



}
