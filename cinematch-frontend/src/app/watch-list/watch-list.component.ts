import { Component, OnInit } from '@angular/core';
import { MovieCardComponentHorizontal } from "../common-component/movie-card-horizontal/movie-card-horizontal.component";
import { Movie } from '../../models/movie';
import { CommonModule, NgFor } from '@angular/common';
import { WatchlistService } from '../../services/watchlist/watchlist.service';
import { LoaderService } from '../../services/loader/loader.service';
import { ToastrService } from 'ngx-toastr';
import { MovieConsultation } from '../../models/movieConsultation';
import { TranslatePipe } from '@ngx-translate/core';

@Component({
  selector: 'app-watch-list',
  imports: [MovieCardComponentHorizontal, CommonModule, TranslatePipe],
  templateUrl: './watch-list.component.html',
  styleUrl: './watch-list.component.scss'
})
export class WatchListComponent implements OnInit {

  movieConsultations: MovieConsultation[] = [];

  constructor(private watchListService: WatchlistService,
    private loaderService: LoaderService,
    private toasterService: ToastrService
  ) {

  }

  ngOnInit(): void {
    this.loaderService.show();
    this.watchListService.getCurrentUserWatchList().subscribe({
      next: (movieConsultation: MovieConsultation[]) => {
        this.movieConsultations = movieConsultation;
      },
      error: (error) => {
        this.toasterService.error(error.error.reason, error.error.error);
      }
    }).add(() => {
      this.loaderService.hide();
    })
  }

  deleteMovie($event: number) {
    this.movieConsultations = this.movieConsultations.filter(el => el.movie.id !== $event);
  }
}
