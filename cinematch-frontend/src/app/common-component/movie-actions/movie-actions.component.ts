import { Component, Input, OnInit, output } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import {TooltipPosition, MatTooltipModule} from '@angular/material/tooltip';
import { TranslatePipe } from '@ngx-translate/core';
@Component({
  selector: 'app-movie-actions',
  imports: [MatIconModule,
    MatButtonModule,
    MatTooltipModule,
    TranslatePipe
  ],
  templateUrl: './movie-actions.component.html',
  styleUrl: './movie-actions.component.scss'
})

export class MovieActionsComponent implements OnInit 
{

  
  likeActionEvent = output<number>();
  watchlistActionEvent = output<number>();
  
  @Input() movieId: number;
  
  ngOnInit(): void {
    
  }

  handleLike(): void {
    this.likeActionEvent.emit(this.movieId);
  }

  handleWatchListAction(): void {
    this.watchlistActionEvent.emit(this.movieId);
  }

}
