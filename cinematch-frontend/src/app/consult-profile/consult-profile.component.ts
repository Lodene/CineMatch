import { Component, inject, OnInit } from '@angular/core';
import { ProfilComponent } from '../profil/profil.component';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Params, Router } from '@angular/router';
import { User } from '../../models/types/components/user/user.model';
import { ToastrService } from 'ngx-toastr';
import { LoaderService } from '../../services/loader/loader.service';
import { ProfileService } from '../../services/profile/profile.service';
import { TranslateService } from '@ngx-translate/core';

@Component({
  selector: 'app-consult-profile',
  imports: [
    CommonModule,
    ProfilComponent],
  templateUrl: './consult-profile.component.html',
  styleUrl: './consult-profile.component.scss'
})
export class ConsultProfileComponent implements OnInit{
  
  private route = inject(ActivatedRoute)
  private loaderService = inject(LoaderService);
  private profileService = inject(ProfileService);
  private toasterService = inject(ToastrService);
  private translateService = inject(TranslateService);

  userProfile = new User();
  picture: string = '/assets/avatar-default.jpg';
  username: string;
  
  ngOnInit(): void {
    this.route.params.subscribe({
      next:((params: Params) => {
        console.log(params);
        const username = params['username'];
        if (username !== null && username !== undefined &&
          username !== ""
        ) {
          this.loadUserProfileByUsername(username);
        } else {
          this.toasterService.error(
            this.translateService.instant('app.common-component.profile.not-found')
          );
        }
      })
    })
  }

  loadUserProfileByUsername(username: string) {
    this.loaderService.show();
    this.profileService.getProfileByUsername(username).subscribe({
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

}
