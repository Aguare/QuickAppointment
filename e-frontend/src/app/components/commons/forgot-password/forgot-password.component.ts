import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NavbarGuestComponent } from '../navbar-guest/navbar-guest.component';
import Swal from 'sweetalert2';
import { Router } from '@angular/router';
import { log } from 'console';

@Component({
  selector: 'app-forgot-password',
  standalone: true,
  imports: [CommonModule, FormsModule, NavbarGuestComponent],
  templateUrl: './forgot-password.component.html',
  styleUrl: './forgot-password.component.scss'
})
export class ForgotPasswordComponent {
  email: string = '';

  constructor(
    private _router: Router
  ) { }

  validEmail(): boolean {
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return emailRegex.test(this.email);
  }

  canSubmit(): boolean {
    return this.validEmail();
  }

  validateEmail(): void {
    if (!this.validEmail()) {
      Swal.fire({
        title: 'Error',
        text: 'Por favor, ingresa un correo electrónico válido.',
        icon: 'error',
        confirmButtonText: 'Ok',
      });
    } else {
      console.log('Correo enviado...');
      
    }
  }

  sendRecoveryEmail(): void {
    if (this.canSubmit()) {
      
    } else {
      Swal.fire({
        title: 'Error',
        text: 'Por favor, ingresa un correo electrónico válido.',
        icon: 'error',
        confirmButtonText: 'Ok',
      });
    }
  }
}
