import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatButtonModule } from '@angular/material/button';
import { ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { AuthService } from '../../services/auth/auth.service';
import { error } from 'console';
import { Router } from '@angular/router';
import { ToasterService } from '../../services/toaster/toaster.service';

type LoginRequest = {
  username: string;
  password: string;
}

@Component({
  selector: 'app-login',
  imports: [MatInputModule, MatFormFieldModule, MatButtonModule, ReactiveFormsModule],
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss'
})
export class LoginComponent {
  loginForm: FormGroup;
  constructor(private fb: FormBuilder,
    private authService: AuthService,
    private router: Router,
    private toasterService: ToasterService
  ) {
    this.loginForm = this.fb.group({
      username: ['', Validators.required],
      password: ['', Validators.required]
    });
  }
  onSubmit(): void {
    if (this.loginForm.valid) {
      const loginRequest = {
        username: this.loginForm.get('username')?.value,
        password: this.loginForm.get('password')?.value
      } as LoginRequest;
      
      this.authService.login(loginRequest.username, loginRequest.password).subscribe(res => {
        // redirect to main page =>
        this.router.navigateByUrl("/main");
      }, (error) => {
        this.toasterService.add(error.error.reason);
        console.log(error.error);
      });
    } else {
      console.log('Form is not valid!');
    }
  }
}
