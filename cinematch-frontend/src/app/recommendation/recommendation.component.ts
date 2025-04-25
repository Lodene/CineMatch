import { Component, inject, OnDestroy, OnInit } from '@angular/core';
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
import { TranslatePipe, TranslateService } from '@ngx-translate/core';
import { MovieRecommendationService } from '../../services/movie/movie-recommendation.service';
import { LoaderService } from '../../services/loader/loader.service';
import { SocketService } from '../../services/socket/socket.service';
import { AuthService } from '../../services/auth/auth.service';
import { ProfileService } from '../../services/profile/profile.service';
import { ToastrService } from 'ngx-toastr';


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
export class RecommendationComponent implements OnInit {

  // movie: Movie;
  recommendedMovies: Movie[]=[];

  // Direct property bindings for slider values
  minTime: number = 120;
  maxTime: number = 200;

  private movieRecommendationService = inject(MovieRecommendationService);
  private loaderService = inject(LoaderService);
  private socketService = inject(SocketService);
  private profileService = inject(ProfileService);
  private toasterService = inject(ToastrService);
  private translateService = inject(TranslateService);

  private username: string = "";

  ngOnInit() {
    this.profileService.currentUsername.pipe(tap((username) => {
      if (username !== null) {
        this.username = username;
        this.socketService.register(this.username)
        this.loaderService.show();
        this.getRecommendations();
        this.socketService.on("recommendation").pipe(
          tap((event) => {
            const recommendationNotification = event as SocketNotification;
            if (this.recommendedMovies.length === 0) {
              this.recommendedMovies = recommendationNotification.recommendedMovies;
              this.toasterService.success(
                this.translateService.instant('app.common-component.recommendation.succes')
              );
            }
            this.loaderService.hide();
          })
        ).subscribe()
      } else {
        this.toasterService.error(
          this.translateService.instant('app.common-component.recommendation.user-error-reason'),
          this.translateService.instant('app.common-component.recommendation.user-error')
        );
      }
    })).subscribe()
  }
  private getRecommendations() {
    this.movieRecommendationService.getRecommendedMovie().subscribe({
      next: ((res: string) => {
        // Contains request ID for debug
        // console.log(res);
        // Event can be triggered multiple time
        if (this.recommendedMovies.length === 0) {
          this.toasterService.show(
            this.translateService.instant('app.common-component.recommendation.show')
            );
        }
      }),
      error: ((err: any) => {
        console.error(err);
        if (err?.error) {
          this.toasterService.error(err.error.reason, err.error.error);
        } else {
          this.toasterService.error(
            this.translateService.instant('app.common-component.recommendation.post-request-error'));  
        }
        this.loaderService.hide();
      })
    })
  }
}
