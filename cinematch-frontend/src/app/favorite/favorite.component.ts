import { Component, OnInit } from '@angular/core';
import { LovedMovieService } from '../../services/loved-movie/loved-movie.service';
import { Observable } from 'rxjs';
import { Movie } from '../../models/movie';
import { LoaderService } from '../../services/loader/loader.service';
import { ToastrService } from 'ngx-toastr';
import { MovieCardComponentHorizontal } from "../common-component/movie-card-horizontal/movie-card-horizontal.component";
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-favorite',
  imports: [MovieCardComponentHorizontal, CommonModule],
  templateUrl: './favorite.component.html',
  styleUrl: './favorite.component.scss'
})
export class FavoriteComponent implements OnInit {

  lovedMovies:Movie[] = [];

  constructor(private lovedMovieService: LovedMovieService,
    private loaderService: LoaderService,
    private toasterService: ToastrService
  ) {
  }

  ngOnInit(): void {
    this.loaderService.show();
    this.lovedMovieService.getCurrentUserLikedMovie().subscribe({
      next: (movies: Movie[]) => {
        this.lovedMovies = movies;
        console.log(this.lovedMovies);
      },
      error: (error) => {
        this.toasterService.error(error.error.reason, error.error.error);
      }
    }).add(()=> {
      this.loaderService.hide();
    })
  }

}
