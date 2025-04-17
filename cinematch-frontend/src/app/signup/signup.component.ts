import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
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
    TranslatePipe, 
    CommonModule],
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
      // username: ['', Validators.required],
      // password: ['', Validators.required],
      // email: ['', Validators.required]
      username: ['', [
        Validators.required,
        Validators.minLength(3),
        Validators.maxLength(50),
        Validators.pattern(/^[a-zA-Z0-9_]+$/)
      ]],
      email: ['', [
        Validators.required,
        Validators.maxLength(100),
        Validators.email
      ]],
      password: ['', [
        Validators.required,
        Validators.minLength(8),
        Validators.maxLength(12),
        Validators.pattern(/^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%^&*]).{8,}$/)
      ]]
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
        this.router.navigate(['login']);
      }, (error) => {
        this.toastr.error(error.error.reason, error.error.error);
        // console.log(error.error);
      });
    } else {
      console.log('Form is not valid!');
    }
  }
}
