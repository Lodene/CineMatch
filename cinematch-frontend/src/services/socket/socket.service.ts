import { Injectable } from '@angular/core';
import { io, Socket } from 'socket.io-client';
import { Observable } from 'rxjs';


@Injectable({
  providedIn: 'root'
})
export class SocketService {

  private socket: Socket;
  private readonly uri: string = 'http://localhost:3000'; // Your backend URL

  constructor() {
    this.socket = io(this.uri);
  }
  emit(eventName: string, data?: any): void {
    this.socket.emit(eventName, data);
  }
  on<T>(eventName: string): Observable<T> {
    return new Observable<T>((subscriber) => {
      this.socket.on(eventName, (data: T) => {
        subscriber.next(data);
      });
    });
  }
  disconnect(): void {
    if (this.socket) {
      this.socket.disconnect();
    }
  }
}
