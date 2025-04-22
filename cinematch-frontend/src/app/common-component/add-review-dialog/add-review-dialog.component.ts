import {ChangeDetectionStrategy, Component, inject, model, signal} from '@angular/core';
import {FormsModule} from '@angular/forms';
import {MatButtonModule} from '@angular/material/button';
import {
  MAT_DIALOG_DATA,
  MatDialog,
  MatDialogActions,
  MatDialogClose,
  MatDialogContent,
  MatDialogRef,
  MatDialogTitle,
} from '@angular/material/dialog';
import {MatFormFieldModule} from '@angular/material/form-field';
import {MatInputModule} from '@angular/material/input';
import { Review } from '../../../models/review';
import { TranslatePipe, TranslateService } from '@ngx-translate/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-add-review-dialog',
  imports: [
    MatFormFieldModule,
    MatInputModule,
    FormsModule,
    MatButtonModule,
    MatDialogTitle,
    MatDialogContent,
    MatDialogActions,
    MatDialogClose,
    TranslatePipe,
    CommonModule
  ],
  templateUrl: './add-review-dialog.component.html',
  styleUrl: './add-review-dialog.component.scss'

})
export class AddReviewDialogComponent {
  

  readonly dialogRef = inject(MatDialogRef<AddReviewDialogComponent>);
  readonly data = inject<any>(MAT_DIALOG_DATA);
  
  // Create a model for the description that's initialized with the data
  readonly description = model(this.data.description);
  


  ngOnInit() {
    console.log('Initial description:', this.description());
    console.log('Dialog data:', this.data.review.id);
  }
  
  onNoClick(): void {
    this.dialogRef.close();
  }
  
  deleteReview(): void {
    this.dialogRef.close('DELETE');
  }

  
}

