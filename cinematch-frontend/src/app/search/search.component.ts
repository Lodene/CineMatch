import { CommonModule } from '@angular/common';
import { AfterViewInit, Component, inject, OnInit, ViewChild } from '@angular/core';
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
import { ToastrService } from 'ngx-toastr';
import { firstValueFrom, map, Observable, startWith, tap } from 'rxjs';
import { Movie } from '../../models/movie';
import { MovieSearchRequest } from '../../models/movie-search-request';
import { LoaderService } from '../../services/loader/loader.service';
import { MovieService } from '../../services/movie/movie.service';
import { MovieCardComponent } from '../common-component/movie-card/movie-card.component';
import { MatPaginator, MatPaginatorModule } from '@angular/material/paginator';
import { PaginatedMovieResponse } from '../../models/paginated-movie-reponse';

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
    MatPaginatorModule
  ],
  templateUrl: './search.component.html',
  styleUrl: './search.component.scss'
})
export class SearchComponent implements OnInit {

  movieService = inject(MovieService);
  loaderService = inject(LoaderService);
  toasterService = inject(ToastrService);


  titleSearch = '';
  actorSearch = '';
  directorSearch = '';
  noteSearch?: number;

  moviesFound: Movie[] = [];

  startYear: number = 1990;
  currentYear: number = new Date().getFullYear();
  endYear: number = 2010;

  genersSearchControl = new FormControl('');
  selectedGeners = new FormControl([]);
  genersOptions: string[] = [];
  filteredOptions: Observable<string[]>;
  currentPage: number = 0;
  totalPages: number = 0;
  totalElements: number = 100;
  hasNextPage: boolean;

  async ngOnInit() {
    this.loaderService.show();
    this.movieService.getAllGenres().subscribe({
      next: (g: string[]) => {
        this.genersOptions = g;
        console.log(this.genersOptions);
      },
      error: (error) => {
        this.toasterService.error(error.error.reason, error.error.error);
      }
    }).add(() => {
      this.loaderService.hide();
    })

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
    console.log(`year range: ${this.startYear} - ${this.endYear}`);
  }

  async getMoviesTest() {
    var cast = null;
    if (!!this.actorSearch && this.actorSearch !== "") {
      cast = [];
      cast.push(this.actorSearch);
    }

    var director = null;
    if (!!this.directorSearch && this.directorSearch !== "") {
      director = [];
      director.push(this.directorSearch);
    }

    var genres = null;
    if(this.selectedGeners.value && this.selectedGeners.value.length > 0){
      genres = this.selectedGeners.value as string[];

    }

    var searchRequest = new MovieSearchRequest();
    searchRequest.title = !!this.titleSearch && this.titleSearch !== "" ? this.titleSearch : null;
    searchRequest.minRating = !!this.noteSearch && this.noteSearch > 1? this.noteSearch : null;
    searchRequest.cast = cast;
    searchRequest.director = director;
    searchRequest.genres = genres;
    searchRequest.startDate = new Date(this.startYear, 0, 1);
    searchRequest.startDate = new Date(this.startYear, 0, 1);
    searchRequest.endDate = new Date(this.endYear, 0, 1);

    this.loaderService.show();
    this.movieService.searchMovies(searchRequest).subscribe({
      next: (m: Movie[]) => {
        this.moviesFound = m;
        console.log(this.moviesFound);
      },
      error: (error) => {
        this.toasterService.error(error.error.reason, error.error.error);
      }
    }).add(() => {
      this.loaderService.hide();
    })
  }

  async getMovies(page: number = 0, size: number = 10) {
    var cast = null;
    if (!!this.actorSearch && this.actorSearch !== "") {
      cast = [];
      cast.push(this.actorSearch);
    }
    
    var director = null;
    if (!!this.directorSearch && this.directorSearch !== "") {
      director = [];
      director.push(this.directorSearch);
    }
    
    var genres = null;
    if(this.selectedGeners.value && this.selectedGeners.value.length > 0){
      genres = this.selectedGeners.value as string[];
    }
    
    var searchRequest = new MovieSearchRequest();
    searchRequest.title = !!this.titleSearch && this.titleSearch !== "" ? this.titleSearch : null;
    searchRequest.minRating = !!this.noteSearch && this.noteSearch > 1? this.noteSearch : null;
    searchRequest.cast = cast;
    searchRequest.director = director;
    searchRequest.genres = genres;
    searchRequest.startDate = new Date(this.startYear, 0, 1);
    searchRequest.endDate = new Date(this.endYear, 0, 1);
    
    this.loaderService.show();
    
    this.movieService.searchMovies(searchRequest, page, size).subscribe({
      next: (response: PaginatedMovieResponse) => {
        this.moviesFound = response.content;
        this.currentPage = response.currentPage;
        this.totalPages = response.totalPages;
        this.totalElements = response.totalElements;
        this.hasNextPage = response.hasNext;
        console.log('Movies found:', this.moviesFound);
        console.log('Pagination info:', {
          currentPage: this.currentPage,
          totalPages: this.totalPages,
          totalElements: this.totalElements,
          hasNext: this.hasNextPage
        });
      },
      error: (error) => {
        this.toasterService.error(error.error.reason, error.error.error);
      }
    }).add(() => {
      this.loaderService.hide();
    });
  }

  private _filterGeners(value: string): string[] {
    const filterValue = value.toLowerCase();
    return this.genersOptions.filter(option => option.toLowerCase().includes(filterValue));
  }

}
