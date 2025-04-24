import { MovieCardComponent } from '../common-component/movie-card/movie-card.component';
import { Movie } from '../../models/movie';
import { MovieCardComponentHorizontal } from '../common-component/movie-card-horizontal/movie-card-horizontal.component';
import { NgFor } from '@angular/common';

import { AfterViewChecked, AfterViewInit, Component, OnInit, ViewChild } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import { FeaturedFilmComponent } from '../featured-film/featured-film.component';
import { CommonModule } from '@angular/common';
import { MovieService } from '../../services/movie/movie.service';
import { finalize, firstValueFrom, tap } from 'rxjs';
import { LoaderService } from '../../services/loader/loader.service';
import { error } from 'console';
import { ToastrService } from 'ngx-toastr';
import { RandomUtils } from '../../utils/randomUtils';
import { PaginatedMovieResponse } from '../../models/paginated-movie-reponse';
import {MatPaginator, MatPaginatorModule, PageEvent} from '@angular/material/paginator';

@Component({
  selector: 'app-home',
  imports: [CommonModule, FeaturedFilmComponent, MovieCardComponent, MatPaginatorModule],
  templateUrl: './home.component.html',
  styleUrl: './home.component.scss'
})
export class HomeComponent implements OnInit, AfterViewInit  {

  film: Movie;
  movies: Movie[] = [];
  error: any;
  currentPage: number = 0;
  totalPages: number = 0;
  totalElements: number = 100;
  hasNext: boolean;

  @ViewChild(MatPaginator) paginator!: MatPaginator;

  constructor(    
    private movieService: MovieService,
    private loaderService: LoaderService,
    private toasterService: ToastrService
  ) {

  }
  ngAfterViewInit(): void {
    setTimeout(() => {
      this.paginator.page
      .pipe(
        tap((event) => {
          this.loadMovie(this.paginator.pageIndex, this.paginator.pageSize);
        })
      )
      .subscribe();
    });
  }


  ngOnInit(): void {
    // temps de recup 5~ mins
    // fixme: add pagination endpoint for movie
    this.loadMovie();
    this.movieService.getMovieCount().subscribe({
      next: ((value: number) => {
        this.totalElements = value;
      }),
      error:((err) => {
        // no toaster, error is outside of functionnal logic
        console.error(err)
      })
    })
  }

  private loadMovie(page: number = 0, size: number = 10) {
    this.loaderService.show();
    this.movieService.getAllMovies(page, size).subscribe({
      next: (res: PaginatedMovieResponse) => {
        this.movies = res.content;
        this.film = this.movies[RandomUtils.getRandomNumberFromRange(0, this.movies.length)];
        this.loaderService.hide();
      },
      error: (error) => {
        console.log(error);
        if (!!error?.error) {
          this.error = error.error;
        }
      },
      complete: () => {
      }
    }).add(() => {
      this.loaderService.hide();
    });
  }
}

