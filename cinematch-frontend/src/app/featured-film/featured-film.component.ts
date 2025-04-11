import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { RouterModule } from '@angular/router';
import { Movie } from '../../models/movie';

@Component({
  selector: 'app-featured-film',
  imports: [CommonModule, MatButtonModule, MatIconModule, RouterModule],
  templateUrl: './featured-film.component.html',
  styleUrl: './featured-film.component.scss'
})
export class FeaturedFilmComponent {
  @Input() movie!: Movie;
}
