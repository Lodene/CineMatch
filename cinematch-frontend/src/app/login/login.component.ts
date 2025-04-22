import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatButtonModule } from '@angular/material/button';
import { ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { AuthService, LoginRequest } from '../../services/auth/auth.service';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { TranslatePipe, TranslateService } from '@ngx-translate/core';
import { LoaderService } from '../../services/loader/loader.service';
import { error } from 'console';
import { LanguageService } from '../../services/language/language.service';



@Component({
  selector: 'app-login',
  imports: [MatInputModule, MatFormFieldModule, MatButtonModule, ReactiveFormsModule, TranslatePipe],
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss'
})
export class LoginComponent {
  loginForm: FormGroup;
  logged: boolean;

  constructor(private fb: FormBuilder,
    private authService: AuthService,
    private router: Router,
    private toastr: ToastrService,
    private translateService: TranslateService,
    private loaderService: LoaderService,
    private toasterService: ToastrService,
    private langService: LanguageService 
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
      this.logged = false;
      this.loaderService.show();
      this.authService.login(loginRequest).subscribe({
        next: (res => {
          this.authService.setTokenInStorage(res.token);
          this.authService.tokenSubject.next(res.token);
          this.langService.setLanguage(res.lang);
          this.toasterService.success(this.translateService.instant('app.common-component.login.response.login-successful'));
          this.router.navigate(['']);
        }),
        error: (err => {
           this.toasterService.error(err.error.reason, err.error.error);
        })
      }).add(() => {
        this.loaderService.hide();
      });         
    } else {
      console.log('Form is not valid!');
    }
  }
}
