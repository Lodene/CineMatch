import { Component, Input } from '@angular/core';
import { Person } from '../../models/types/components/person/person.model';
import { MatIconModule } from '@angular/material/icon';  // Importation du module MatIcon

@Component({
  selector: 'app-person-card',
  imports: [MatIconModule],  // Ajoutez ici MatIconModule
  templateUrl: './person-card.component.html',
  styleUrl: './person-card.component.scss'
})
export class PersonCardComponent {
  @Input() person: Person;
}
