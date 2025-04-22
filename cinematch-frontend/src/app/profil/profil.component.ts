import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatIconModule } from '@angular/material/icon';
import { MatTabsModule } from '@angular/material/tabs';
import { MatCardModule } from '@angular/material/card';
import { MatDividerModule } from '@angular/material/divider';
import { MatButtonModule } from '@angular/material/button';
import { RouterModule } from '@angular/router';
import { TranslatePipe } from '@ngx-translate/core';
import { User } from '../../models/types/components/user/user.model';
import { Movie } from '../../models/movie';
import { ProfileService } from '../../services/profile/profile.service';
import { LoaderService } from '../../services/loader/loader.service';
import { ToastrService } from 'ngx-toastr';
import { HistoryComponent } from "../history/history.component";



@Component({
  selector: 'app-profil',
  imports: [
    CommonModule,
    RouterModule,
    MatIconModule,
    MatTabsModule,
    MatCardModule,
    MatDividerModule,
    MatButtonModule,
    TranslatePipe,
    HistoryComponent
],  
  templateUrl: './profil.component.html',
  styleUrls: ['./profil.component.scss']

})
export class ProfilComponent implements OnInit {
  favoriteMovies: Movie[] = [];

  userProfile: User =  new User();   // Stocke le profil récupéré depuis l'API
  userLoaded: boolean = false;
  
  constructor(
    private profileService: ProfileService,
    private loaderService: LoaderService,
    private toasterService: ToastrService
  ) {}

  ngOnInit(): void {
    this.loadUserProfile();
  }

  loadUserProfile() {
    this.loaderService.show();
    this.profileService.getProfil().subscribe({
      next: (profile: User) => {
        this.userProfile = profile;
      },
      error: (error) => {
        this.toasterService.error(error.error.reason, error.error.error);
      },
      complete: () => {
        this.loaderService.hide();
        this.userLoaded = true;
      }
    });
    this.profileService.usernameSubject.subscribe({
      next: (username: string | null) => {
        console.log(username);
      }
    })
  }
  

}
