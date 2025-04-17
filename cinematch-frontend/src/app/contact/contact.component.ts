import { Component, ViewEncapsulation } from '@angular/core';
import { TranslatePipe } from '@ngx-translate/core';
import { CommonModule } from '@angular/common';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { FormsModule } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';

@Component({
  selector: 'app-contact',
  imports: [
    CommonModule, 
    MatFormFieldModule, 
    TranslatePipe, 
    MatInputModule,
    FormsModule,
    MatButtonModule
  ],
  // encapsulation: ViewEncapsulation.None,
  templateUrl: './contact.component.html',
  styleUrl: './contact.component.scss'
})
export class ContactComponent {
  contact = {
    username: '',
    email: '',
    message: ''
  };

  onSubmit() {
    console.log('Contact form submitted:', this.contact);
    // Envoyer les données ici à la DB 
  }
}
