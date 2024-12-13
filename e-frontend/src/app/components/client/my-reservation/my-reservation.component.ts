import { Component, OnInit } from '@angular/core';
import { NavbarComponent } from '../../commons/navbar/navbar.component';
import { MatToolbarModule } from '@angular/material/toolbar';
import { ClientService } from '../../../services/client.service';
import { MyAppointment } from '../../../interfaces/interfaces';
import { CommonModule } from '@angular/common';
import { LocalStorageService } from '../../../services/local-storage.service';
import { Router } from '@angular/router';

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
    private router: Router
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

  viewBill(appointment: MyAppointment) {
    console.log(appointment);

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

    this.router.navigate(['/client/bill'])
    
  }

  saveBill(body: any) {
    const dataBill = this.localStorageService.getItem('data_bill');
    if (dataBill) {
    } else {
      body.cui = '0000000000';
      body.nit = 'Consumidor Final';
      body.direction = 'Ciudad';

      this.localStorageService.setItem('bill', body);
    }
  }
}
