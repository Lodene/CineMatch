import {ChangeDetectionStrategy, Component, inject, model, OnChanges, OnInit, signal, SimpleChanges} from '@angular/core';
import {FormBuilder, FormGroup, FormsModule, Validators} from '@angular/forms';
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
import { StarRatingComponent } from "../star-rating/star-rating.component";


export type AddReviewDialogData = {
  review: Review;
  description: string;
  note: number;
}

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
    TranslatePipe,
    CommonModule,
    MatDialogClose,
    StarRatingComponent,
],
  templateUrl: './add-review-dialog.component.html',
  styleUrl: './add-review-dialog.component.scss'

})
export class AddReviewDialogComponent implements OnInit {
  
  
  ngOnInit(): void {
    this.isFormValid = this.verifiyForm();
  }
  

  // dialog
  readonly dialogRef = inject(MatDialogRef<AddReviewDialogComponent>);
  readonly data = inject<AddReviewDialogData>(MAT_DIALOG_DATA);  
  readonly description = model(this.data.description);
  readonly userRating = model(this.data.note)

  isFormValid: boolean = false;

  onNoClick(): void {
    this.dialogRef.close();
  }
  
  deleteReview(): void {
    this.dialogRef.close('DELETE');
  }

  descriptionChanged($event: Event) {
    console.log($event);
    this.isFormValid = this.verifiyForm();
  }

  /**
   * 
   * @returns true if the form is valid
   */
  verifiyForm(): boolean {
    return this.description().length > 0 && this.userRating() > 0;
  }
  
}

