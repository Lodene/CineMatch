import { Component, Input, OnChanges, OnInit, SimpleChanges } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { RouterModule } from '@angular/router';
import { Movie } from '../../models/movie';
import { MovieImageUtils } from '../../utils/movieImageUtils';
import { TranslatePipe } from '@ngx-translate/core';

@Component({
  selector: 'app-featured-film',
  imports: [CommonModule, MatButtonModule, MatIconModule, RouterModule, TranslatePipe],
  templateUrl: './featured-film.component.html',
  styleUrl: './featured-film.component.scss'
})
export class FeaturedFilmComponent implements OnInit, OnChanges {
  
  constructor() {
  }
  
  ngOnChanges(changes: SimpleChanges): void {
    console.log(changes);
    this.backdropPath = MovieImageUtils.constructUrl(this.movie.backdropPath);
  }
  
  @Input() movie: Movie;
  backdropPath: string;
  ngOnInit(): void {
  // Movie can be null when component is mounting 
    if (!!this.movie) {
      this.backdropPath = MovieImageUtils.constructUrl(this.movie.backdropPath);
    }
  }
  
}
