import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class UserConfigService {

  backendUrl: string = 'http://localhost:8081/user/preferences'

  constructor(private http: HttpClient) {
  }

  public updateLang(lang: string): Observable<void> {
    return this.http.put<void>(`${this.backendUrl}/lang/${lang}`, {});
  }

}
