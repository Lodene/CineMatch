import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatIconModule } from '@angular/material/icon';
import { MatTabsModule } from '@angular/material/tabs';
import { MatCardModule } from '@angular/material/card';
import { MatDividerModule } from '@angular/material/divider';
import { MatButtonModule } from '@angular/material/button';
import { RouterModule } from '@angular/router';
import { TranslatePipe, TranslateService } from '@ngx-translate/core';
import { User } from '../../models/types/components/user/user.model';
import { Movie } from '../../models/movie';
import { ProfileService } from '../../services/profile/profile.service';
import { LoaderService } from '../../services/loader/loader.service';
import { ToastrService } from 'ngx-toastr';
import { HistoryComponent } from "../history/history.component";
import { MatDialog } from '@angular/material/dialog';
import { EditProfileDialogComponent } from '../common-component/edit-profile-dialog/edit-profile-dialog.component';

@Component({
  selector: 'app-profil',
  standalone: true,
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
  userProfile: User = new User();
  picture = '/assets/avatar-default.jpg';
  readonly dialog = inject(MatDialog);

  constructor(
    private profileService: ProfileService,
    private loaderService: LoaderService,
    private toasterService: ToastrService,
    private translateService: TranslateService
  ) {}

  ngOnInit(): void {
    this.loadUserProfile();
  }

  loadUserProfile() {
    this.loaderService.show();
    this.profileService.getProfil().subscribe({
      next: (profile: User) => {
        this.userProfile = profile;
        if(!!this.userProfile.profilPicture) {
          this.picture = 'data:image/jpeg;base64,' + this.userProfile.profilPicture
        } else {
          this.picture = '/assets/avatar-default.jpg';
        }
      },
      error: (error) => {
        this.toasterService.error(error.error.reason, error.error.error);
      }
    }).add(() => {
      this.loaderService.hide();
    });
  }

  editProfile() {
    const dialogRef = this.dialog.open(EditProfileDialogComponent, {
      width: '80%',
      data: { profile: this.userProfile }
    });

    dialogRef.afterClosed().subscribe({
      next: (res) => {
        if (!res) return;

        this.loaderService.show();

        const { profile, picture, deletePicture } = res;

        const updateProfileCall = () => this.profileService.updateProfile(profile).subscribe({
          next: () => {
            this.toasterService.success(
              this.translateService.instant('app.common-component.profile.update-successfuly.reason'),
              this.translateService.instant('app.common-component.profile.update-successfuly.message')
            );
            this.loadUserProfile(); // Refresh
          },
          error: (err) => {
            console.error(err);
            this.toasterService.error(err.error.reason, err.error.error);
          },
          complete: () => this.loaderService.hide()
        });

        if (deletePicture && !picture) {
          this.profileService.deletePicture().subscribe({
            next: () => updateProfileCall(),
            error: (err) => {
              console.error(err);
              this.toasterService.error(err.error.reason, err.error.error);
              this.loaderService.hide();
            }
          });
        } else {
          updateProfileCall();

          if (picture) {
            const formData = new FormData();
            formData.append('file', picture);

            this.profileService.updatePicture(formData).subscribe({
              error: (err) => {
                console.error(err);
                this.toasterService.error(err.error.reason, err.error.error);
              }
            });
          }
        }
      }
    });
  }
}
