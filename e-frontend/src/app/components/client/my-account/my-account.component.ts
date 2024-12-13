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
    private clientService: ClientService
  ) {}

  ngOnInit(): void {
    const idUser = this.localStorageService.getItem('id_user');

    this.form = this.fb.group({
      nit: ['', [Validators.required]],
      cui: ['', [Validators.required, Validators.maxLength(13)]],
      direction: ['', [Validators.required]],
    });

    if (idUser) {
      this.clientService.getBillingsByUser(idUser).subscribe({
        next: (data: Billing) => {
          if (data) {
            
            this.hasData = true; 
            this.idBilling = data.id
            this.form.setValue({
              nit: data.nit,
              cui: data.cui,
              direction: data.direction,
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
}
