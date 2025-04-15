import { Component, Input } from '@angular/core';
import { Person } from '../../models/types/components/person/person.model';

@Component({
  selector: 'app-person-card',
  imports: [],
  templateUrl: './person-card.component.html',
  styleUrl: './person-card.component.scss'
})
export class PersonCardComponent {
  @Input() person: Person;

}
