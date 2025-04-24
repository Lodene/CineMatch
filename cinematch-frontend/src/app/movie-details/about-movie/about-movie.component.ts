import { Component, Input } from '@angular/core';
import { Movie } from '../../../models/movie';
import { CommonModule } from '@angular/common';
import { TranslatePipe } from '@ngx-translate/core';

@Component({
  selector: 'app-about-movie',
  imports: [
    CommonModule,
    TranslatePipe
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
