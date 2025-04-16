import { Component, Input, OnInit } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { Movie } from '../../../models/movie';
import { MatIconModule } from '@angular/material/icon';
import { CommonModule, NgFor } from '@angular/common';
import { MovieImageUtils } from '../../../utils/movieImageUtils';
import { TranslatePipe } from '@ngx-translate/core';

@Component({
  selector: 'movie-card-horizontal',
  imports: [
    MatIconModule,
    MatCardModule,
    MatButtonModule,
    NgFor,
    CommonModule,
    TranslatePipe
  ],
  templateUrl: './movie-card-horizontal.component.html',
  styleUrl: './movie-card-horizontal.component.scss',
  standalone: true
})
export class MovieCardComponentHorizontal implements OnInit {

  @Input() movie: Movie;
  posterPath: string;

  ngOnInit(): void {
    this.posterPath = MovieImageUtils.constructUrl(this.movie.posterPath);
  }

  

}
