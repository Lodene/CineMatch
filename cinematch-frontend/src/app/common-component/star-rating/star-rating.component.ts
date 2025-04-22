import { NgClass, NgFor } from '@angular/common';
import { Component, Input, Output, EventEmitter } from '@angular/core';

@Component({
  selector: 'app-star-rating',
  imports: [NgFor, NgClass],
  templateUrl: './star-rating.component.html',
  styleUrls: ['./star-rating.component.scss']
})
export class StarRatingComponent {
  @Input() rating = 0;
  @Output() ratingChange = new EventEmitter<number>();

  currentHover = 0;

  setRating(value: number) {
    this.rating = value;
    this.ratingChange.emit(this.rating);
  }

  onMouseMove(event: MouseEvent, starIndex: number) {
    const { offsetX, target } = event;
    const width = (target as HTMLElement).clientWidth;
    const isHalf = offsetX < width / 2;
    this.currentHover = starIndex - (isHalf ? 0.5 : 0);
  }

  clearHover() {
    this.currentHover = 0;
  }

  getStarIcon(star: number): string {
    const value = this.currentHover || this.rating;
    if (value >= star) return 'star';
    if (value >= star - 0.5) return 'star_half';
    return 'star_border';
  }
}
