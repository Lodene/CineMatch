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

@Component({
  selector: 'app-history',
  imports: [
    MatTabsModule,
    CommonModule,
    MatCardModule,
    MatIconModule,
    MatDividerModule,
    TranslateModule,
    HistoryEntryComponent
],
  templateUrl: './history.component.html',
  styleUrl: './history.component.scss'
})
export class HistoryComponent {

  profileService = inject(ProfileService);
  
  constructor(
    private _historyService: HistoryService,
    private _movieService: MovieService,
  ) { }

  history : PaginatedHistoryResponse;
  ngOnInit() {
    this._historyService.getCurrentUserMovieHistory().subscribe({
      next: (result: PaginatedHistoryResponse) => {
        this.history = result;
        console.log(result);
      }, error: (err => {
        console.error(err);
      }),
    }).add(() => {

    });
  }
}
