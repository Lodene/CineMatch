import { Movie } from '../../models/movie';
import { MovieCardComponent } from '../common-component/movie-card/movie-card.component';

import { CommonModule } from '@angular/common';
import { AfterViewInit, Component, OnInit, ViewChild } from '@angular/core';
import { MatPaginator, MatPaginatorModule } from '@angular/material/paginator';
import { TranslatePipe } from '@ngx-translate/core';
import { ToastrService } from 'ngx-toastr';
import { tap } from 'rxjs';
import { PaginatedMovieResponse } from '../../models/paginated-movie-reponse';
import { LoaderService } from '../../services/loader/loader.service';
import { MovieService } from '../../services/movie/movie.service';
import { RandomUtils } from '../../utils/randomUtils';
import { FeaturedFilmComponent } from '../featured-film/featured-film.component';

@Component({
  selector: 'app-home',
  imports: [
    CommonModule,
    FeaturedFilmComponent,
    MovieCardComponent,
    MatPaginatorModule,
    TranslatePipe
  ],
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

