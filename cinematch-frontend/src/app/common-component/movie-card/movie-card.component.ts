import { Component, Input } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { Movie } from '../../../models/movie';
import { MatIconModule } from '@angular/material/icon';

@Component({
  selector: 'movie-card',
  imports: [
    MatIconModule,
    MatCardModule,
    MatButtonModule
  ],
  templateUrl: './movie-card.component.html',
  styleUrl: './movie-card.component.scss',
  standalone: true
})
export class MovieCardComponent {
  @Input() movie: Movie;

  

}
