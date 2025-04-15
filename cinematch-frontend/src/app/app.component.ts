import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { CommonModule } from '@angular/common'; // Pour *ngIf, *ngFor, etc.
import { HeaderComponent } from './common-component/header/header.component';
import { FooterComponent } from './common-component/footer/footer.component';

import {
  TranslateService,
  TranslatePipe,
  TranslateDirective
} from "@ngx-translate/core";
import { LoaderComponent } from './common-component/loader/loader.component';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, CommonModule, HeaderComponent, FooterComponent, LoaderComponent],
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss'
})


export class AppComponent {
  title: string = "";
  constructor(private translate: TranslateService) {
    this.translate.addLangs(['fr', 'en']);
    this.translate.setDefaultLang('en');
    this.translate.use('en');
    this.title = this.translate.instant("app.title");
  }
}
