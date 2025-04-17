import { CommonModule, NgIf } from '@angular/common';
import { Component, ViewEncapsulation } from '@angular/core';
import { MatIconModule } from '@angular/material/icon';
import { ActivatedRoute } from '@angular/router';
import { finalize, firstValueFrom } from 'rxjs';
import { Movie } from '../../models/movie';
import { MovieService } from '../../services/movie/movie.service';
import { LoaderService } from '../../services/loader/loader.service';
import { ToastrService } from 'ngx-toastr';
import { TranslateDirective, TranslatePipe } from '@ngx-translate/core';
import { ListPersonComponent } from '../list-person/list-person.component';
import { MovieConsultation } from '../../models/movieConsultation';
import { MovieImageUtils } from '../../utils/movieImageUtils';
import { MatTabsModule } from '@angular/material/tabs';
import { AboutMovieComponent } from "./about-movie/about-movie.component";
import { ReviewCardComponent } from "../common-component/review-card/review-card.component";
import { MatButtonModule } from '@angular/material/button';

@Component({
  selector: 'app-movie-details',
  imports: [
    NgIf,
    MatIconModule,
    CommonModule,
    TranslatePipe,
    ListPersonComponent,
    MatTabsModule,
    AboutMovieComponent,
    ReviewCardComponent,
  ],
  encapsulation: ViewEncapsulation.None,
  templateUrl: './movie-details.component.html',
  styleUrl: './movie-details.component.scss'
})
export class MovieDetailsComponent {

  constructor(
    private movieService: MovieService,
    private route: ActivatedRoute,
    private loaderService: LoaderService,
    private toasterService: ToastrService
  ) { }

  idMovie: number;
  movie: Movie;
  movieConsultation: MovieConsultation;

  // used for image
  backdropUrl: string;
  posterUrl: string


  ngOnInit() {
    this.loaderService.show();
    this.route.params.subscribe(async params => {
      this.idMovie = +params['id'];
      this.movieService.getMovieById(this.idMovie).pipe(finalize(() => { this.loaderService.hide() })).subscribe((movieConsultation: MovieConsultation) => {
        this.movieConsultation = movieConsultation;
        this.movie = movieConsultation.movie;
        this.backdropUrl = MovieImageUtils.constructUrl(movieConsultation.movie.backdropPath);
        this.posterUrl = MovieImageUtils.constructUrl(movieConsultation.movie.posterPath)
      }, error => {
        this.toasterService.error(error.error.reason, error.error.error);
      });
    }
    )
  }
}
