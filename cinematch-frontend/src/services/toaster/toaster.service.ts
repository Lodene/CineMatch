import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class ToasterService {

  toasts: string[] = [];

  constructor() { }


  add(message: string) {
    this.toasts.push(message);
  }

  remove(index: number) {
    this.toasts.splice(index, 1);
  }
}
