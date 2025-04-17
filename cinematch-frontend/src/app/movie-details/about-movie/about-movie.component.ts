import { Component, Input } from '@angular/core';
import { Movie } from '../../../models/movie';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-about-movie',
  imports: [
    CommonModule
  ],
  templateUrl: './about-movie.component.html',
  styleUrl: './about-movie.component.scss'
})
export class AboutMovieComponent {

  @Input() movie : Movie;

  ngOnInit() {
    console.log(this.movie);

  }
}
