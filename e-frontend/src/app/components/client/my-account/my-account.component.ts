import { User } from './../../../interfaces/interfaces';
import { Component } from '@angular/core';
import { NavbarComponent } from '../../commons/navbar/navbar.component';
import {
  FormBuilder,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { CommonModule } from '@angular/common';
import { LocalStorageService } from '../../../services/local-storage.service';
import { ClientService } from '../../../services/client.service';
import { Billing } from '../../../interfaces/interfaces';
import Swal from 'sweetalert2';
import { UserService } from '../../../services/user.service';

@Component({
  selector: 'app-my-account',
  standalone: true,
  imports: [NavbarComponent, ReactiveFormsModule, CommonModule],
  templateUrl: './my-account.component.html',
  styleUrl: './my-account.component.scss',
})
export class MyAccountComponent {
  form!: FormGroup;
  hasData = false;
  idBilling: number | undefined;

  constructor(
    private fb: FormBuilder,
    private localStorageService: LocalStorageService,
    private clientService: ClientService,
    private _userService: UserService
  ) { }

  ngOnInit(): void {
    const idUser = this.localStorageService.getUserId();

    this.form = this.fb.group({
      nit: ['', [Validators.required]],
      cui: ['', [Validators.required, Validators.maxLength(13)]],
      direction: ['', [Validators.required]],
      twoFactorAuth: [false],
    });

    if (idUser) {
      this.getStatus2FA();
      this.clientService.getBillingsByUser(idUser).subscribe({
        next: (data: Billing) => {
          if (data) {

            this.hasData = true;
            this.idBilling = data.id
            this.form.setValue({
              nit: data.nit,
              cui: data.cui,
              direction: data.direction
            });
          } else {
            this.hasData = false;
          }
        },
        error: (err) => {
          console.error('Error al obtener los datos:', err);
          this.hasData = false;
        },
      });
    }
  }

  onSubmit(): void {
    if (this.form.valid) {
      const updatedData = this.form.value;
      const userId = this.localStorageService.getItem('id_user');
      updatedData.fkUser = userId;

      if (this.hasData) {
        this.clientService.updateBilling(this.idBilling!, updatedData).subscribe({
          next: (value) => {
            Swal.fire({
              position: "top-end",
              icon: "success",
              title: "Datos actualizados correctamente",
              showConfirmButton: false,
              timer: 1500
            });
          },
          error: (err) => {
            console.error('Error al actualizar los datos:', err);
          },
        });
      } else {
        this.clientService.saveBilling(updatedData).subscribe({
          next: (value) => {
            Swal.fire({
              position: "top-end",
              icon: "success",
              title: "Los datos se guardaron correctamente",
              showConfirmButton: false,
              timer: 1500
            });
            this.hasData = true;
          },
          error: (err) => {
            console.error('Error al guardar los datos:', err);
          },
        });
      }

      setTimeout(() => {
        window.location.reload();
      }, 1500)
    }
  }

  getStatus2FA(): void {
    const idUser = this.localStorageService.getUserId();

    this._userService.isActivated2FA({ idUser: idUser }).subscribe({
      next: (value: any) => {
        this.form.patchValue({
          twoFactorAuth: value
        });
      },
      error: (err: any) => {
        console.error('Error al obtener el estado de la autenticación de dos factores:', err);
      },
    });
  }

  activate2FA(): void {
    const isChecked = !this.form.get('twoFactorAuth')?.value;

    const title = isChecked
      ? '¿Está seguro de activar la autenticación de dos factores?'
      : '¿Está seguro de desactivar la autenticación de dos factores?';

    const text = isChecked
      ? 'Se enviará un correo electrónico con un código de verificación cada vez que inicie sesión. Sin este código no podrá acceder a su cuenta. ¿Desea continuar?'
      : 'Esto deshabilitará el requerimiento de un código de verificación al iniciar sesión. ¿Desea continuar?';

    Swal.fire({
      title,
      text,
      showDenyButton: true,
      confirmButtonText: `Sí`,
      denyButtonText: `No`,
    }).then((result) => {
      if (result.isConfirmed) {
        const idUser = {
          idUser: Number(this.localStorageService.getUserId())
        };

        this._userService.activate2FA(idUser).subscribe({
          next: (value: any) => {
            const successMessage = isChecked
              ? 'Se ha activado la autenticación de dos factores'
              : 'Se ha desactivado la autenticación de dos factores';

            Swal.fire({
              position: 'top-end',
              icon: 'success',
              title: successMessage,
              showConfirmButton: false,
              timer: 1500
            });
          },
          error: (err: any) => {
            console.error('Error al cambiar el estado de la autenticación de dos factores:', err);

            const errorMessage = isChecked
              ? 'Error al activar la autenticación de dos factores'
              : 'Error al desactivar la autenticación de dos factores';

            Swal.fire({
              position: 'top-end',
              icon: 'error',
              title: errorMessage,
              showConfirmButton: false,
              timer: 1500
            });

            this.form.patchValue({
              twoFactorAuth: !isChecked
            });
          },
        });
      } else {
        this.form.patchValue({
          twoFactorAuth: !isChecked
        });
      }
    });
  }


}
