import { CommonModule } from '@angular/common';
import { Component, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import {
  FormBuilder,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { RegisterModalComponent } from '../register-modal/register-modal.component';
import { MatIconModule } from '@angular/material/icon';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { NavbarGuestComponent } from '../navbar-guest/navbar-guest.component';
import { CookieService } from 'ngx-cookie-service';

import Swal from 'sweetalert2';
import { Router } from '@angular/router';
import { error, log } from 'console';
import { UserService } from '../../../services/user.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    RegisterModalComponent,
    MatIconModule,
    MatProgressSpinnerModule,
    NavbarGuestComponent,
  ],
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss',
  providers: [CookieService],
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
})
export class LoginComponent {
  loginForm: FormGroup;
  registerForm: FormGroup;
  hide = true;
  isModalVisible = false;
  registerModalTitle = '¡Regístrate!';
  logoUrl = '';
  hidePassword = true;
  isLoading = false;
  isLoginMode = false;

  ngOnInit(): void {}

  constructor(
    private fb: FormBuilder,
    private _router: Router,
    private _cookieService: CookieService,
    private userService: UserService
  ) {
    this.loginForm = this.fb.group({
      usernameOrEmail: ['', Validators.required],
      password: ['', Validators.required],
    });

    this.registerForm = this.fb.group(
      {
        email: ['', [Validators.required, Validators.email]],
        username: ['', Validators.required],
        password: ['', [Validators.required, Validators.minLength(6)]],
        confirmPassword: ['', Validators.required],
      },
      { validators: this.passwordMatchValidator }
    );
  }

  // Validator to password
  passwordMatchValidator(form: FormGroup) {
    const password = form.get('password')?.value || '';
    const confirmPassword = form.get('confirmPassword')?.value || '';
    return password === confirmPassword ? null : { passwordMismatch: true };
  }

  openRegisterModal() {
    this.isModalVisible = true;
    document.body.classList.add('is-modal-active');
  }

  closeRegisterModal() {
    this.isModalVisible = false;
    document.body.classList.remove('is-modal-active');
    this.isLoading = false;
  }

  onRegister() {
    if (this.registerForm.valid) {
      const data = {
        email: this.registerForm.get('email')?.value,
        username: this.registerForm.get('username')?.value,
        password: this.registerForm.get('password')?.value,
      };

      this.userService.register(data).subscribe({
        next: (data: any) => {
          Swal.fire({
            position: 'top-end',
            icon: 'success',
            title: data.message,
            showConfirmButton: false,
            timer: 1500,
          });
          this.closeRegisterModal();
        },
        error: (err) => {
          console.log(err);
        },
      });

      this.registerForm.reset();
    } else {
      Swal.fire('Error', 'Por favor, complete los campos requeridos', 'error');
      this.isLoading = false;
      this.registerForm.reset();
      return;
    }
  }

  get usernameOrEmailHasErrorRequired() {
    return this.loginForm.get('usernameOrEmail')?.hasError('required');
  }

  get passwordHasErrorRequired() {
    return this.loginForm.get('password')?.hasError('required');
  }
  togglePasswordVisibility() {
    this.hidePassword = !this.hidePassword;
  }

  forgotPassword() {
    this._router.navigate(['/forgot-password']);
  }

  onLogin() {
    if (this.loginForm.invalid) {
      Swal.fire('Error', 'Por favor, complete los campos requeridos', 'error');
      return;
    }

    const data = {
      username: this.loginForm.get('usernameOrEmail')?.value,
      password: this.loginForm.get('password')?.value,
    };

    this.userService.login(data).subscribe({
      next: (resp: any) => {
        if (resp.idRole == 2) {
        } else {
          Swal.fire({
            position: 'top-end',
            icon: 'success',
            title: resp.message,
            showConfirmButton: false,
            timer: 1500,
          });
          setTimeout(() => {
            this._router.navigate(['/admin/init']);
          }, 1500);
        }
      },
      error: (error) => {
        console.log(error);
      },
    });
  }
}
