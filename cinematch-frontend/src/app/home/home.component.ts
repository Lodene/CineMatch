import { MovieCardComponent } from '../common-component/movie-card/movie-card.component';
import { Movie } from '../../models/movie';
import { MovieCardComponentHorizontal } from '../common-component/movie-card-horizontal/movie-card-horizontal.component';
import { NgFor } from '@angular/common';

import { AfterViewChecked, AfterViewInit, Component, OnInit } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import { FeaturedFilmComponent } from '../featured-film/featured-film.component';
import { CommonModule } from '@angular/common';
import { MovieService } from '../../services/movie/movie.service';
import { finalize, firstValueFrom } from 'rxjs';
import { LoaderService } from '../../services/loader/loader.service';
import { error } from 'console';
import { ToastrService } from 'ngx-toastr';
import { RandomUtils } from '../../utils/randomUtils';

@Component({
  selector: 'app-home',
  imports: [CommonModule, FeaturedFilmComponent, MovieCardComponent],
  templateUrl: './home.component.html',
  styleUrl: './home.component.scss'
})
export class HomeComponent implements OnInit, AfterViewChecked  {

  film: Movie;
  movies: Movie[] = [];
  error: any;
  constructor(    
    private movieService: MovieService,
    private loaderService: LoaderService,
    private toasterService: ToastrService
  ) {

  }
  ngAfterViewChecked(): void {
    if (!!this.movies) {
      this.loaderService.hide();
    }
  }

  ngOnInit(): void {
    // temps de recup 5~ mins
    // fixme: add pagination endpoint for movie
    this.loaderService.show();
    this.movieService.getAllMovies().subscribe({
      next: (res: Movie[]) => {
        this.movies = res;
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
      // this.loaderService.hide();
    });
  }


  ngOnChanges() {
    if (this.error) {
      this.toasterService.error(this.error.reason, this.error.error);
    }
  }
}

