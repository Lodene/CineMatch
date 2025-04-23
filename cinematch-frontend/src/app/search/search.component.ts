import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormControl, FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatDividerModule } from '@angular/material/divider';
import { MatExpansionModule } from '@angular/material/expansion';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatSliderModule } from '@angular/material/slider';
import { TranslatePipe } from '@ngx-translate/core';
import { map, Observable, startWith } from 'rxjs';
import { Movie } from '../../models/movie';
import { MovieCardComponent } from '../common-component/movie-card/movie-card.component';

@Component({
  selector: 'app-search',
  imports: [
    CommonModule,
    TranslatePipe,
    MovieCardComponent,
    MatSliderModule,
    FormsModule,
    MatFormFieldModule,
    MatSelectModule,
    ReactiveFormsModule,
    MatInputModule,
    MatIconModule,
    MatDividerModule,
    MatButtonModule,
    MatExpansionModule,
  ],
  templateUrl: './search.component.html',
  styleUrl: './search.component.scss'
})
export class SearchComponent {

  titleSearch = '';
  actorSearch = '';
  directorSearch = '';
  yearSearch?: number;
  notrSearch?: number;

  moviesFound: Movie[] = [];

  minYear: number = 1990;
  currentYear: number = new Date().getFullYear();
  maxYear: number = 2010;

  genersSearchControl = new FormControl('');
  selectedGeners = new FormControl([]);
  genersOptions: string[] = ['Action', 'Adventure', 'Comedy', 'Drama', 'Horror', 'Romance', 'Sci-Fi', 'Thriller'];
  filteredOptions: Observable<string[]>;


  ngOnInit() {
    this.filteredOptions = this.genersSearchControl.valueChanges.pipe(
      startWith(''),
      map(value => this._filterGeners(value || ''))
    );
  }




  onGenersClosedPanel() {
    this.genersSearchControl.setValue('');
  }

  onGenersSelectionChange() {
    console.log(this.selectedGeners.value);
  }

  // Optional: method to emit values when they change
  onSliderChange(): void {
    // You could emit these values to a parent component or service
    console.log(`year range: ${this.minYear} - ${this.maxYear}`);
  }

  getMovies() {

    console.log(this.titleSearch, this.actorSearch, this.directorSearch, this.selectedGeners.value);

  }

  private _filterGeners(value: string): string[] {
    const filterValue = value.toLowerCase();
    return this.genersOptions.filter(option => option.toLowerCase().includes(filterValue));
  }

}
