import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { PaginatedHistoryResponse } from '../../models/history/paginated-history-response';

@Injectable({
    providedIn: 'root'
})
export class HistoryService {
    backendUrl: string = 'http://localhost:8081/history';

    constructor(private http: HttpClient) { }

    public getUserMovieHistoryByUsername(userName: string): Observable<PaginatedHistoryResponse> {
        return this.http.get<PaginatedHistoryResponse>(`${this.backendUrl}/history/${userName}`);
    }

    public getCurrentUserMovieHistory(): Observable<PaginatedHistoryResponse> {
        return this.http.get<PaginatedHistoryResponse>(`${this.backendUrl}/history`);
    }

}
