import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import Swal from 'sweetalert2';
import { AdminService } from '../../../services/admin.service';
import { NavbarGuestComponent } from '../navbar-guest/navbar-guest.component';

@Component({
  selector: 'app-verify-email',
  standalone: true,
  imports: [NavbarGuestComponent],
  templateUrl: './verify-email.component.html',
  styleUrl: './verify-email.component.scss'
})
export class VerifyEmailComponent {
  token: string = '';
  email: string = '';

  constructor(
    private _activatedRoute: ActivatedRoute,
    private _emailService: AdminService,
    private _router: Router
  ) {
    this._activatedRoute.paramMap.subscribe(params => {
      this.token = params.get('token') || '';
      this.email = params.get('email') || '';
    });
  }

  verifyEmail(): void {
    this._emailService.verifyEmailUser({ token: this.token, email: this.email }).subscribe({
      next: (res: any) => {
        Swal.fire({
          title: 'Éxito',
          text: res.message,
          icon: 'success',
          confirmButtonText: 'Ok'
        });
        this._router.navigate(['/login']);
      },
      error: (err: any) => {
        console.error(err);
        Swal.fire({
          title: 'Error',
          text: "Este enlace ya no es válido " + err.error.message,
          icon: 'error',
          confirmButtonText: 'Ok'
        });
        this._router.navigate(['/login']);
      }
    });
  }
}
