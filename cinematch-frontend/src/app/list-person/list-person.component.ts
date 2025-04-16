import { Component, Input, OnInit } from '@angular/core';
import { PersonCardComponent } from '../person-card/person-card.component';
import { Person } from '../../models/types/components/person/person.model';
import { CommonModule } from '@angular/common';


@Component({
  selector: 'app-list-person',
  imports: [PersonCardComponent, CommonModule],
  templateUrl: './list-person.component.html',
  styleUrl: './list-person.component.scss'
})
export class ListPersonComponent implements OnInit {

  @Input() person: string[]
  @Input() title: string = '';
  formattedPersonList: Person[];
  ngOnInit(): void {
    this.formattedPersonList = this.formatPersonList();    
  }
  
  formatPersonList(): Person[] {
    const formatedArray: Person[] = [];
    this.person.forEach(person => {
      // fixme => fetch image with imdb api ?
      formatedArray.push({
        name: person,
        imageUrl: ""
      } as Person)
    })
    return formatedArray;
  }

}
