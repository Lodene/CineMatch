import { CommonModule } from '@angular/common';
import { Component, Input } from '@angular/core';
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

@Component({
  selector: 'app-history',
  imports: [
    MatTabsModule,
    CommonModule,
    MatCardModule,
    MatIconModule,
    MatDividerModule,
    TranslateModule
  ],
  templateUrl: './history.component.html',
  styleUrl: './history.component.scss'
})
export class HistoryComponent {

  constructor(
    private _historyService: HistoryService,
    private _movieService: MovieService,
  ) { }

  movieActionType = MovieActionType;
  @Input() user: User;
  history : PaginatedHistoryResponse;
  async ngOnInit() {
    if (this.user.username?.length > 0) {
      this.history = await firstValueFrom(this._historyService.getUserMovieHistoryByUsername(this.user.username));
    }
  }
}
