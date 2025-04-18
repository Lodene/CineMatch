import { Component } from '@angular/core';
import { TranslatePipe } from '@ngx-translate/core';
import { MatButtonModule } from '@angular/material/button';
import { LanguageService } from '../../services/language/language.service';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatSelectChange, MatSelectModule } from '@angular/material/select';

@Component({
  selector: 'app-settings',
  imports: [TranslatePipe, MatButtonModule, MatFormFieldModule, MatSelectModule],
  templateUrl: './settings.component.html',
  styleUrl: './settings.component.scss'
})
export class SettingsComponent {

  selectedLang = '';

  constructor(public langService: LanguageService) {}

  changeLanguage($event: MatSelectChange) {
    console.log("selected lang: ", $event);
    if ($event?.value) {
      this.selectedLang = $event.value  
      this.langService.setLanguage(this.selectedLang);
    }
  }

  save() {
  }

}
