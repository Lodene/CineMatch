import { Component } from '@angular/core';
import { TranslatePipe } from '@ngx-translate/core';
import { MatButtonModule } from '@angular/material/button';
import { LanguageService } from '../../services/language/language.service';

@Component({
  selector: 'app-settings',
  imports: [TranslatePipe, MatButtonModule],
  templateUrl: './settings.component.html',
  styleUrl: './settings.component.scss'
})
export class SettingsComponent {
  constructor(public langService: LanguageService) {}

  changeLanguage() {
    const nextLang = this.langService.currentLang === 'fr' ? 'en' : 'fr';
    this.langService.setLanguage(nextLang);
  }

}
