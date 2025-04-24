import { Component, OnInit } from '@angular/core';
import { MovieCardComponentHorizontal } from "../common-component/movie-card-horizontal/movie-card-horizontal.component";
import { Movie } from '../../models/movie';
import { CommonModule, NgFor } from '@angular/common';
import { DiaryService } from '../../services/diary/diary.service';
import { LoaderService } from '../../services/loader/loader.service';
import { ToastrService } from 'ngx-toastr';
import { MovieConsultation } from '../../models/movieConsultation';
import { TranslatePipe } from '@ngx-translate/core';

@Component({
  selector: 'app-diary',
  imports: [MovieCardComponentHorizontal, CommonModule, TranslatePipe],
  templateUrl: './diary.component.html',
  styleUrl: './diary.component.scss'
})
export class DiaryComponent implements OnInit {

  watchedMovies: MovieConsultation[] = [];

  constructor(private diaryService: DiaryService,
              private loaderService: LoaderService,
              private toasterService: ToastrService
  ) {

  }

  ngOnInit(): void {
    this.loaderService.show();
    this.diaryService.getCurrentUserWatchedMovie().subscribe({
      next: (movies: MovieConsultation[]) => {
        this.watchedMovies = movies;
        console.log(this.watchedMovies);
      },
      error: (error) => {
        this.toasterService.error(error.error.reason, error.error.error);
      }
    }).add(()=> {
      this.loaderService.hide();
    })
  }

  deleteMovie($event: number) {
    this.watchedMovies = this.watchedMovies.filter(el => el.movie.id !== $event);
  }
}
