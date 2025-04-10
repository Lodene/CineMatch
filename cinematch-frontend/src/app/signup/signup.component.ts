import { Component } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { AuthService, SignupRequest } from '../../services/auth/auth.service';
import { ToastrService } from 'ngx-toastr';
import { Router } from '@angular/router';
import { TranslatePipe, TranslateService } from '@ngx-translate/core';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';



@Component({
  selector: 'app-signup',
  imports: [MatInputModule,
    MatFormFieldModule,
    MatButtonModule,
    ReactiveFormsModule,
    TranslatePipe],
  templateUrl: './signup.component.html',
  styleUrl: './signup.component.scss'
})
export class SignupComponent {
  signupForm: FormGroup;
  constructor(private fb: FormBuilder,
    private authService: AuthService,
    private router: Router,
    private toastr: ToastrService,
    private translateService: TranslateService
  ) {
    this.signupForm = this.fb.group({
      // todo: Add regex for email & username :
      username: ['', Validators.required],
      password: ['', Validators.required],
      email: ['', Validators.required]
    });
  }

  onSubmit(): void {
    if (this.signupForm.valid) {
      const signupRequest = {
        username: this.signupForm.get('username')?.value,
        password: this.signupForm.get('password')?.value,
        email: this.signupForm.get('email')?.value
      } as SignupRequest;
      this.authService.signup(signupRequest).subscribe(res => {
        // redirect to login page =>
        this.toastr.success(this.translateService.instant('app.common-component.signup.response.signup-successful'));
        this.router.navigateByUrl("/login");
      }, (error) => {
        this.toastr.error(error.error.reason, error.error.error);
        // console.log(error.error);
      });
    } else {
      console.log('Form is not valid!');
    }
  }
}
