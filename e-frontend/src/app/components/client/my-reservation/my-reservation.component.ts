import { Component, OnInit } from '@angular/core';
import { NavbarComponent } from '../../commons/navbar/navbar.component';
import { MatToolbarModule } from '@angular/material/toolbar';
import { ClientService } from '../../../services/client.service';
import { MyAppointment } from '../../../interfaces/interfaces';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-my-reservation',
  standalone: true,
  imports: [NavbarComponent, MatToolbarModule, CommonModule],
  templateUrl: './my-reservation.component.html',
  styleUrl: './my-reservation.component.scss',
})
export class MyReservationComponent implements OnInit {
  appointments: MyAppointment[] = [];

  constructor(private clientService: ClientService) {}

  ngOnInit(): void {
    this.clientService.getMyAppointments().subscribe({
      next: (value: MyAppointment[]) => {
        this.appointments = value;
      },
      error: (err) => {
        console.log(err);
      },
    });
  }
}
