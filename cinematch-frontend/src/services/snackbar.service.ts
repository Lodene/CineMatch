import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';
import { MatSnackBar, MatSnackBarHorizontalPosition, MatSnackBarVerticalPosition } from '@angular/material/snack-bar';

@Injectable({
  providedIn: 'root'
})
export class SnackbarService {

  constructor(private snackBar: MatSnackBar) { }

  show(message: string, action: string = 'OK', 
    horizontalPosition: MatSnackBarHorizontalPosition = 'right',
    verticalPosition: MatSnackBarVerticalPosition = 'top',
    duration: number = 3000): void {
      this.snackBar.open(message, action, {
        duration,
        horizontalPosition,
        verticalPosition,
      });
  }
}
