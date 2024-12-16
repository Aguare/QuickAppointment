import { Component, OnInit } from '@angular/core';
import { NavbarComponent } from '../../commons/navbar/navbar.component';
import { MatToolbarModule } from '@angular/material/toolbar';
import { ClientService } from '../../../services/client.service';
import {
  Billing,
  Company,
  MyAppointment,
  Service,
} from '../../../interfaces/interfaces';
import { CommonModule } from '@angular/common';
import { LocalStorageService } from '../../../services/local-storage.service';
import { Router } from '@angular/router';
import Swal from 'sweetalert2';
import { AdminService } from '../../../services/admin.service';
import { forkJoin } from 'rxjs';

@Component({
  selector: 'app-my-reservation',
  standalone: true,
  imports: [NavbarComponent, MatToolbarModule, CommonModule],
  templateUrl: './my-reservation.component.html',
  styleUrl: './my-reservation.component.scss',
})
export class MyReservationComponent implements OnInit {
  appointments: MyAppointment[] = [];

  constructor(
    private clientService: ClientService,
    private localStorageService: LocalStorageService,
    private router: Router,
    private adminService: AdminService
  ) {}

  ngOnInit(): void {
    const fkUser = this.localStorageService.getItem('id_user');

    this.clientService.getMyAppointments(+fkUser).subscribe({
      next: (value: MyAppointment[]) => {
        this.appointments = value;
      },
      error: (err) => {
        console.log(err);
      },
    });
  }

  editBill(appointment: MyAppointment) {
    forkJoin({
      company: this.adminService.getCompanyById(appointment.fkCompany),
      service: this.adminService.getTypeAppointemById(appointment.fkService),
    }).subscribe({
      next: ({ company, service }) => {
        this.localStorageService.setItem('actualService', service);

        const route = company.courtRental
          ? '/client/courtReservation'
          : '/client/reservation';

        this.router.navigate([route], {
          queryParams: {
            id: appointment.fkCompany,
            appointment: appointment.id,
          },
        });
      },
      error: (err) => {
        console.error(err);
      },
    });
  }

  cancelBill(appointment: MyAppointment) {
    Swal.fire({
      title: '¿Quieres cancelar tu cita?',
      text: 'No habrá posibilidad de recuperarla',
      icon: 'warning',
      showCancelButton: true,
      confirmButtonColor: '#3085d6',
      cancelButtonColor: '#d33',
      confirmButtonText: 'Si',
    }).then((result) => {
      if (result.isConfirmed) {
        this.clientService.cancelAppointment(appointment.id).subscribe({
          next: (value:any) => {
            Swal.fire({
              position: "top-end",
              icon: "success",
              title: value.message,
              showConfirmButton: false,
              timer: 1500
            });

            setTimeout(()=>{window.location.reload()}, 1500)
          },
          error: (err) => {
            console.log(err);
          },
        });
      }
    });
  }

  viewBill(appointment: MyAppointment) {
    const billBody = {
      date: appointment.date,
      fkUser: 1,
      hour: appointment.hour,
      employee: appointment.first_name + '' + appointment.last_name,
      type: appointment.service,
      place: appointment.place,
      price: appointment.price,
      idCompany: appointment.fkCompany,
    };

    this.saveBill(billBody);
  }

  saveBill(body: any) {
    const idUser = this.localStorageService.getItem('id_user');
    this.clientService.getBillingsByUser(idUser).subscribe({
      next: (value: Billing) => {
        if (value) {
          body.cui = value.cui;
          body.nit = value.nit;
          body.direction = value.direction;
        } else {
          body.cui = '0000000000';
          body.nit = 'Consumidor Final';
          body.direction = 'Ciudad';
        }
        this.localStorageService.setItem('bill', body);
        this.router.navigate(['/client/bill']);
      },
      error: (err) => {
        body.cui = '0000000000';
        body.nit = 'Consumidor Final';
        body.direction = 'Ciudad';
        this.localStorageService.setItem('bill', body);
        this.router.navigate(['/client/bill']);
      },
    });
  }
}
