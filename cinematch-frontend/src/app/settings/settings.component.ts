import { Component } from '@angular/core';
import { TranslatePipe, TranslateService } from '@ngx-translate/core';
import { MatButtonModule } from '@angular/material/button';
import { LanguageService } from '../../services/language/language.service';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatSelectChange, MatSelectModule } from '@angular/material/select';
import { UserConfigService } from '../../services/user-config/user-config.service';
import { LoaderService } from '../../services/loader/loader.service';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-settings',
  imports: [TranslatePipe, MatButtonModule, MatFormFieldModule, MatSelectModule],
  templateUrl: './settings.component.html',
  styleUrl: './settings.component.scss'
})
export class SettingsComponent {

  selectedLang = '';

  constructor(public langService: LanguageService,
    private userConfigService: UserConfigService,
    private loaderService: LoaderService,
    private toasterService: ToastrService,
    private translateService: TranslateService
  ) {}

  changeLanguage($event: MatSelectChange) {
    if ($event?.value) {
      this.selectedLang = $event.value  
    }
  }

  save() {
    this.loaderService.show();
    this.userConfigService.updateLang(this.selectedLang).subscribe({
      next: () => {
        this.langService.setLanguage(this.selectedLang);
        // fixme: dictionnary key
        this.toasterService.success(this.translateService.instant('app.common-component.settings.api-responses.update-successful-description'), 
        this.translateService.instant('app.common-component.settings.api-responses.update-successful-title'));
      },
      error: (err) => {
        this.toasterService.error(err.error.reason, err.error.error);
      }
    }).add(()=> {
      this.loaderService.hide();
    });
  }

}
