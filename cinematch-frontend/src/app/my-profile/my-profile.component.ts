import { CommonModule } from '@angular/common';
import { Component, inject, OnInit } from '@angular/core';
import { ProfilComponent } from '../profil/profil.component';
import { User } from '../../models/types/components/user/user.model';
import { LoaderService } from '../../services/loader/loader.service';
import { ProfileService } from '../../services/profile/profile.service';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-my-profile',
  imports: [
    CommonModule,
    ProfilComponent
  ],
  templateUrl: './my-profile.component.html',
  styleUrl: './my-profile.component.scss'
})
export class MyProfileComponent implements OnInit{

  loaderService = inject(LoaderService);
  profileService = inject(ProfileService);
  toasterService = inject(ToastrService);

  userProfile: User = new User();
  picture: string = '/assets/avatar-default.jpg';
  username: string;

  ngOnInit(): void {
    this.loadUserProfile();
  }

  loadUserProfile() {
    this.loaderService.show();
    this.profileService.getProfil().subscribe({
      next: (profile: User) => {
        this.userProfile = {...profile};
        this.username = this.userProfile.username;
        if (!!this.userProfile.profilPicture) {
          this.picture = 'data:image/jpeg;base64,' + this.userProfile.profilPicture;
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
  /**
   * refresh profile
   */
  handleProfileEdited() {
    this.loadUserProfile();
  }
}
