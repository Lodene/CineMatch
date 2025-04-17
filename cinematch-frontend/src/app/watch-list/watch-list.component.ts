import { Component, OnInit } from '@angular/core';
import { MovieCardComponentHorizontal } from "../common-component/movie-card-horizontal/movie-card-horizontal.component";
import { Movie } from '../../models/movie';
import { NgFor } from '@angular/common';
import { WatchlistService } from '../../services/watchlist/watchlist.service';
import { LoaderService } from '../../services/loader/loader.service';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-watch-list',
  imports: [MovieCardComponentHorizontal, NgFor],
  templateUrl: './watch-list.component.html',
  styleUrl: './watch-list.component.scss'
})
export class WatchListComponent implements OnInit {

  movies: Movie[] = [];

  constructor(private watchListService: WatchlistService,
    private loaderService: LoaderService,
    private toasterService: ToastrService
  ) {

  }

  ngOnInit(): void {
    this.loaderService.show();
    this.watchListService.getCurrentUserWatchList().subscribe({
      next: (movies: Movie[]) => {
        this.movies = movies;
      },
      error: (error) => {
        this.toasterService.error(error.error.reason, error.error.error);
      }
    }).add(() => {
      this.loaderService.hide();
    })
  }
}
