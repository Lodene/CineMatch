import { CommonModule, NgIf } from '@angular/common';
import { Component } from '@angular/core';
import { MatIconModule } from '@angular/material/icon';
import { ActivatedRoute } from '@angular/router';
import { firstValueFrom } from 'rxjs';
import { Movie } from '../../models/movie';
import { MovieService } from '../../services/movie/movie.service';

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

    this.route.params.subscribe(async params => {
      this.idMovie = +params['id'];
      try {
        this.movie = await firstValueFrom(this.movieService.getMovieById(this.idMovie));
        console.log(this.movie);
      } catch (error) {
        this.movie = await firstValueFrom(this.movieService.addMovie(new Movie()));
        console.error(`Error fetching movie with ID ${this.idMovie}:`, error);
      }
    }
    )
  }

}
