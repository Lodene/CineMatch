import { Component, Input } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { Movie } from '../../../models/movie';
import { MatIconModule } from '@angular/material/icon';
import { NgFor } from '@angular/common';

@Component({
  selector: 'movie-card-horizontal',
  imports: [
    MatIconModule,
    MatCardModule,
    MatButtonModule,
    NgFor
  ],
  templateUrl: './movie-card-horizontal.component.html',
  styleUrl: './movie-card-horizontal.component.scss',
  standalone: true
})
export class MovieCardComponentHorizontal {
  @Input() movie: Movie;

  

}
