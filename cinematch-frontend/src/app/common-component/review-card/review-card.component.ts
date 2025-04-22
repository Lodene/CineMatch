import { Component, Input } from '@angular/core';
import { Review } from '../../../models/review';
import { NgFor, CommonModule } from '@angular/common';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { TranslatePipe } from '@ngx-translate/core';
import { Movie } from '../../../models/movie';
import { firstValueFrom } from 'rxjs';

@Component({
  selector: 'app-review-card',
  imports: [
    MatIconModule,
    MatCardModule,
    MatButtonModule,
    CommonModule,
    TranslatePipe
  ],
  templateUrl: './review-card.component.html',
  styleUrl: './review-card.component.scss'
})
export class ReviewCardComponent {

  @Input() review : Review;
  @Input() isProfile : boolean;

  ngOnInit(){
    console.log(this.review)
  }

}
