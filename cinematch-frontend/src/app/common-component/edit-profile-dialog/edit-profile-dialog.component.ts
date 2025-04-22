import { Component, inject, model, signal } from '@angular/core';
import { MatDialogModule } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { TranslatePipe } from '@ngx-translate/core';
import {
  MAT_DIALOG_DATA,
  MatDialog,
  MatDialogActions,
  MatDialogClose,
  MatDialogContent,
  MatDialogRef,
  MatDialogTitle,
} from '@angular/material/dialog';
import {MatCheckboxModule} from '@angular/material/checkbox';
import { FormsModule } from '@angular/forms';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { CommonModule } from '@angular/common';
import { User } from '../../../models/types/components/user/user.model';

export interface ProfileDialogData {
  profile: User;
}

@Component({
  selector: 'app-edit-profile-dialog',
  imports: [
    MatDialogModule,
    MatFormFieldModule,
    FormsModule,
    MatInputModule,
    MatButtonModule,
    CommonModule,
    MatCheckboxModule,
    TranslatePipe
  ],
  templateUrl: './edit-profile-dialog.component.html',
  styleUrl: './edit-profile-dialog.component.scss'
})
export class EditProfileDialogComponent {
  readonly dialogRef = inject(MatDialogRef<EditProfileDialogComponent>);
  readonly data = inject<ProfileDialogData>(MAT_DIALOG_DATA);
  readonly description = model(this.data.profile.description);
  readonly isChild = model(this.data.profile.child)

  save() {
    this.data.profile.description = this.description();
    this.data.profile.child = this.isChild();
    this.dialogRef.close(this.data.profile)
  }

  cancel() {
    this.dialogRef.close();
  }
}
