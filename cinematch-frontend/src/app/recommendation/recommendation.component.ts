import { Component, inject } from '@angular/core';
import { MovieCardComponent } from "../common-component/movie-card/movie-card.component";
import { Movie } from '../../models/movie';
import { MatSliderModule } from '@angular/material/slider';
import { FormControl, FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatIconModule } from '@angular/material/icon';
import { map, Observable, startWith, take, tap } from 'rxjs';
import { AsyncPipe, NgFor, NgIf } from '@angular/common';
import { MatDividerModule } from '@angular/material/divider';
import { MatButtonModule } from '@angular/material/button';
import { TranslatePipe } from '@ngx-translate/core';
import { MovieRecommendationService } from '../../services/movie/movie-recommendation.service';
import { LoaderService } from '../../services/loader/loader.service';
import { SocketService } from '../../services/socket/socket.service';


type SocketNotification = {
  requestId: string;
  fromUsername: string;
  recommendedMovies: Movie[];
}

@Component({
  selector: 'app-recommendation',
  imports: [
    NgFor, NgIf,
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
    TranslatePipe
  ],
  templateUrl: './recommendation.component.html',
  styleUrl: './recommendation.component.scss'
})
export class RecommendationComponent {

  // movie: Movie;
  recommendedMovies: Movie[]=[];


  // Direct property bindings for slider values
  minTime: number = 120;
  maxTime: number = 200;

  genersSearchControl = new FormControl('');
  selectedGeners = new FormControl([]);
  genersOptions: string[] = ['Action', 'Adventure', 'Comedy', 'Drama', 'Horror', 'Romance', 'Sci-Fi', 'Thriller'];
  filteredOptions: Observable<string[]>;

  languageSearchControl = new FormControl('');
  selectedLanguages = new FormControl([]);
  langueagesOptions: string[] = ['French', 'English', 'Spanish', 'Korean', 'Chinese'];
  filteredLanguagesOptions: Observable<string[]>;

  private movieRecommendationService = inject(MovieRecommendationService);
  private loaderService = inject(LoaderService);
  private socketService = inject(SocketService);



  ngOnInit() {
    // this.movie = new Movie();
    // this.filteredOptions = this.genersSearchControl.valueChanges.pipe(
    //   startWith(''),
    //   map(value => this._filterGeners(value || ''))
    // );

    // this.filteredLanguagesOptions = this.languageSearchControl.valueChanges.pipe(
    //   startWith(''),
    //   map(value => this._filterLanguages(value || ''))
    // );
    this.socketService.on("notif").pipe(
            tap((event) => {
              console.log(event);
              this.loaderService.hide();
            })
    ).subscribe();
    this.getRecommendations();
  }

  // Optional: method to emit values when they change
  onSliderChange(): void {
    // You could emit these values to a parent component or service
    console.log(`Runtime range: ${this.minTime} - ${this.maxTime} minutes`);
  }


  private _filterGeners(value: string): string[] {
    const filterValue = value.toLowerCase();
    return this.genersOptions.filter(option => option.toLowerCase().includes(filterValue));
  }
  private _filterLanguages(value: string): string[] {
    const filterValue = value.toLowerCase();
    return this.langueagesOptions.filter(option => option.toLowerCase().includes(filterValue));
  }


  // Optional: for clearing search field when the panel is closed
  onClosedPanel() {
    this.genersSearchControl.setValue('');
    this.languageSearchControl.setValue('');
  }

  onSelectionChange() {
    console.log('Selection changed');
    console.log(this.selectedGeners.value);
    console.log(this.selectedLanguages.value);
    // Add any additional logic needed when selection changes
  }

  private getRecommendations() {
    this.loaderService.show();
    this.movieRecommendationService.getRecommendedMovie().subscribe({
      next: ((res: string) => {

      }),
      error: ((err: any) => {

      })
    })
  }

  filterRecommendations(){
    //TODO : filter recommendedMovies list 
  }
}
