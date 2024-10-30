import { Component } from '@angular/core';
import Swal from 'sweetalert2';
import {
  Appointment,
  Company,
  Employee,
  Place,
  Schedule,
  Service,
} from '../../../interfaces/interfaces';
import { ActivatedRoute, Router } from '@angular/router';
import { LocalStorageService } from '../../../services/local-storage.service';
import { AdminService } from '../../../services/admin.service';
import { ClientService } from '../../../services/client.service';
import { forkJoin } from 'rxjs';
import {
  MatDatepickerInputEvent,
  MatDatepickerModule,
} from '@angular/material/datepicker';
import {
  convertTo24HourFormat,
  formatDate,
  formatHour,
  generateTimeSlots,
  parseTime,
} from '../../../helpers/helpers';
import { CommonModule, DatePipe } from '@angular/common';
import { MatToolbar } from '@angular/material/toolbar';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { NavbarComponent } from '../../commons/navbar/navbar.component';
import { MatInputModule } from '@angular/material/input';

@Component({
  selector: 'app-court-reservation',
  standalone: true,
  imports: [
    CommonModule,
    DatePipe,
    MatDatepickerModule,
    MatToolbar,
    MatButtonModule,
    MatFormFieldModule,
    NavbarComponent,
    MatInputModule,
  ],
  templateUrl: './court-reservation.component.html',
  styleUrl: './court-reservation.component.scss',
})
export class CourtReservationComponent {
  idCompany: number | undefined;
  service: Service | null = null;
  selectedDate: Date | null = null;
  selectedSlot: string | null = null;
  availableSlots: string[] = [];
  minDate!: Date;
  dataCompany: Company | undefined;
  employees: Employee[] = [];
  placeSelected: Place | null = null;
  places: Place[] = [];

  schedules: Schedule[] = [];
  isAvailable: boolean = true;

  constructor(
    private route: ActivatedRoute,
    private localStorageService: LocalStorageService,
    private router: Router,
    private adminService: AdminService,
    private clientService: ClientService
  ) {}

  ngOnInit(): void {
    this.route.queryParams.subscribe((params) => {
      this.idCompany = params['id'] ? +params['id'] : undefined;
    });

    this.service = this.localStorageService.getItem('actualService');
    this.minDate = new Date();

    if (this.idCompany) {
      forkJoin({
        schedules: this.adminService.getScheduleCompany(this.idCompany),
        dataCompany: this.adminService.getCompanyById(this.idCompany),
        employees: this.adminService.getEmployeesCompany(this.idCompany),
        places: this.adminService.getPlacesCompany(this.idCompany),
      }).subscribe({
        next: (result) => {
          this.schedules = result.schedules;
          this.dataCompany = result.dataCompany;
          this.employees = result.employees;
          this.places = result.places;

          if (
            this.employees.length == 0 ||
            this.places.length == 0 ||
            this.schedules.length == 0
          ) {
            this.isAvailable = false;
          }
        },
        error: (err) => {
          console.log(err);
        },
      });
    }
  }

  goBack() {
    this.router.navigate(['/client/company'], {
      queryParams: { id: this.idCompany },
    });
  }

  onDateChange(event: MatDatepickerInputEvent<Date>) {
    this.selectedDate = event.value;
    this.resetSelection();
    if (this.selectedDate) {
      let dayOfWeek = this.selectedDate.getDay();
      if (dayOfWeek == 0) {
        dayOfWeek = 7;
      }

      this.updateAvailableSlots(dayOfWeek);
    }
  }

  updateAvailableSlots(dayOfWeek: number) {
    const schedule = this.schedules.find((s) => s.fkDay === dayOfWeek);

    if (schedule) {
      const openingHour = parseTime(schedule.openingTime);
      const closingHour = parseTime(schedule.closingTime);
      this.availableSlots = generateTimeSlots(openingHour, closingHour);
    } else {
      this.availableSlots = [];
    }
  }

  setSchedule(slot: string) {
    this.selectedSlot = slot;
  }

  selectEmployee(index: number): void {
    this.placeSelected = this.places[index];

    if (this.selectedDate) {
      let appointments: Appointment[] = [];
      this.clientService
        .getAppointmetsByDate(formatDate(this.selectedDate))
        .subscribe({
          next: (value: Appointment[]) => {
            appointments = value.filter(
              (appointment) => this.placeSelected?.id === appointment.fkPlace
            );
            
            const hours = appointments.map((appointment) => appointment.hour);
            const hoursNumber = hours.map((h) => parseTime(h));
            const fH = hoursNumber.map((h) => formatHour(h));
            this.availableSlots = this.availableSlots.filter(
              (slot) => !fH.includes(slot)
            );
          },
          error: (err) => {},
        });
    }
  }

  selectRandomEmployee(): void {
    const randomIndex = Math.floor(Math.random() * this.places.length);
    this.placeSelected = this.places[randomIndex];
    if (this.selectedDate) {
      let appointments: Appointment[] = [];
      this.clientService
        .getAppointmetsByDate(formatDate(this.selectedDate))
        .subscribe({
          next: (value: Appointment[]) => {
            appointments = value.filter(
              (appointment) => this.placeSelected?.id === appointment.fkPlace
            );
            const hours = appointments.map((appointment) => appointment.hour);
            const hoursNumber = hours.map((h) => parseTime(h));
            const fH = hoursNumber.map((h) => formatHour(h));
            this.availableSlots = this.availableSlots.filter(
              (slot) => !fH.includes(slot)
            );
          },
          error: (err) => {},
        });
    }
  }

  resetSelection(): void {
    this.placeSelected = null;
    this.selectedSlot = null;
    this.availableSlots = [];
  }

  saveReservation() {
    if (
      this.selectedDate &&
      this.selectedSlot &&
      this.placeSelected &&
      this.service
    ) {
      const date = this.selectedDate;
      const hour = convertTo24HourFormat(this.selectedSlot);
      const fkUser = this.localStorageService.getUserId();
      const fkPlace = this.placeSelected.id;
      const fkType = this.service.id;
      const randomIndex = Math.floor(Math.random() * this.employees.length);
      const selectedEmploye = this.employees[randomIndex];
      console.log(selectedEmploye);
      
      const fkEmployee = selectedEmploye.id;

      const body = {
        date,
        fkUser,
        hour,
        fkEmployee,
        fkType,
        fkPlace,
      };

      Swal.fire({
        title: '¿Estas seguro de realizar la reservación?',
        showDenyButton: true,
        showCancelButton: true,
        confirmButtonText: 'Confirmar',
        denyButtonText: `No Confirmar`,
      }).then((result) => {
        if (result.isConfirmed) {
          this.clientService.saveAppointment(body).subscribe({
            next: (value: any) => {
              Swal.fire({
                position: 'top-end',
                icon: 'success',
                title: value.message,
                showConfirmButton: false,
                timer: 1500,
              });

              setTimeout(() => {
                this.router.navigate(['/client/company'], {
                  queryParams: { id: this.idCompany },
                });
              }, 1500);
            },
            error: (err) => {
              console.log(err);
              Swal.fire({
                icon: 'error',
                title: 'Oops...',
                text: 'Error al guardar tu reservación',
              });
            },
          });
        } else if (result.isDenied) {
          Swal.fire({
            position: 'top-end',
            icon: 'warning',
            title: 'Tu cita no se guardo',
            showConfirmButton: false,
            timer: 1500,
          });
        }
      });
    }
  }
}
