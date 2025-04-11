import { Component } from '@angular/core';
import { MovieService } from '../../services/movie/movie.service';
import { ActivatedRoute } from '@angular/router';
import { Movie } from '../../models/movie';
import { MovieCardComponentHorizontal } from "../common-component/movie-card-horizontal/movie-card-horizontal.component";
import { CommonModule, NgIf } from '@angular/common';
import { MatIconModule } from '@angular/material/icon';

@Component({
  selector: 'app-movie-details',
  imports: [
    NgIf,
    MatIconModule,
    CommonModule
  ],
  templateUrl: './movie-details.component.html',
  styleUrl: './movie-details.component.scss'
})
export class MovieDetailsComponent {

  constructor(
    private movieService: MovieService,
    private route: ActivatedRoute,
  ) { }

  idMovie: number;
  movie: Movie;
  ngOnInit() {

    this.route.params.subscribe(params => {
      this.idMovie = +params['id'];
      this.movieService.getMovieById(this.idMovie).subscribe(
        (data) => {
          this.movie = data;
          console.log(this.movie);
        },
        (error) => {
          console.error(`Error fetching movie with ID ${this.idMovie}:`, error);
        }
      );
    }
    )
  }

}
