import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatSelectModule } from '@angular/material/select';
import {
  FormArray,
  FormBuilder,
  FormControl,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { EditScheduleModalComponent } from '../edit-schedule-modal/edit-schedule-modal.component';
import { LocalStorageService } from '../../../services/local-storage.service';
import { NavbarComponent } from '../../commons/navbar/navbar.component';
import { MatToolbarModule } from '@angular/material/toolbar';
import { Schedule } from '../../../interfaces/interfaces';
import { ActivatedRoute, Route, Router } from '@angular/router';
import { AdminService } from '../../../services/admin.service';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-config-schedule',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    CommonModule,
    MatFormFieldModule,
    MatSelectModule,
    NavbarComponent,
    MatToolbarModule,
  ],
  templateUrl: './config-schedule.component.html',
  styleUrl: './config-schedule.component.scss',
})
export class ConfigScheduleComponent implements OnInit {
  scheduleForm!: FormGroup;
  scheduleArray: Schedule[] = [];
  idCompany: number | undefined;

  daysOfWeek = [
    { id: 1, name: 'Lunes' },
    { id: 2, name: 'Martes' },
    { id: 3, name: 'Miércoles' },
    { id: 4, name: 'Jueves' },
    { id: 5, name: 'Viernes' },
    { id: 6, name: 'Sábado' },
    { id: 7, name: 'Domingo' },
  ];

  constructor(
    private fb: FormBuilder,
    public dialog: MatDialog,
    private localStorageService: LocalStorageService,
    private route: ActivatedRoute,
    private adminService: AdminService,
    private routerN: Router,
  ) {}

  ngOnInit() {
    this.route.queryParams.subscribe((params) => {
      this.idCompany = params['id'] ? +params['id'] : undefined;
    });

    this.scheduleForm = this.fb.group({
      day: ['', Validators.required],
      openingTime: ['', Validators.required],
      closingTime: ['', Validators.required],
    });

    if (this.idCompany) {
      this.adminService.getScheduleCompany(this.idCompany).subscribe({
        next: (value: Schedule[]) => {
          this.scheduleArray = value.map((schedule) => {
            return {
              ...schedule,
              openingTime: this.removeSeconds(schedule.openingTime),
              closingTime: this.removeSeconds(schedule.closingTime),
            };
          });
        },
        error: (err) => {
          Swal.fire({
            icon: 'error',
            title: 'Oops...',
            text: 'Error al Obtener los horarios',
          });
        },
      });
    }
  }

  removeSeconds(time: string): string {
    return time.slice(0, 5);
  }

  addDay() {
    const fkDay = this.scheduleForm.get('day')?.value;
    const openingTime = this.scheduleForm.get('openingTime')?.value;
    const closingTime = this.scheduleForm.get('closingTime')?.value;
    const fkCompany = this.idCompany!;

    if (this.scheduleArray.some((schedule) => schedule.fkDay === fkDay)) {
      alert('El día ya está agregado.');
      return;
    }

    if (openingTime == closingTime) {
      alert('Es igual.');
      return;
    }

    this.scheduleArray.push({ fkCompany, fkDay, openingTime, closingTime });

    this.scheduleArray.sort((a, b) => a.fkDay - b.fkDay);

    this.scheduleForm.reset();
  }
  deleteDay(index: number) {
    this.scheduleArray.splice(index, 1);
  }

  openEditModal(dayIndex: number) {
    const dialogRef = this.dialog.open(EditScheduleModalComponent, {
      width: '400px',
      data: { ...this.scheduleArray[dayIndex] },
    });

    dialogRef.afterClosed().subscribe((result) => {
      if (result) {
        this.scheduleArray[dayIndex] = result;
      }
    });
  }

  getDay(day: number) {
    const dayName = this.daysOfWeek.find((d) => d.id === day)?.name;
    return dayName;
  }

  saveChanges() {
    if (this.scheduleArray.length > 0) {
      if (this.idCompany) {
        this.adminService
          .saveSchedule(this.idCompany, this.scheduleArray)
          .subscribe({
            next: (value: any) => {
              Swal.fire({
                position: 'top-end',
                icon: 'success',
                title: value.message,
                showConfirmButton: false,
                timer: 1500,
              });

              setTimeout(() => {
                window.location.reload();
              }, 1500);
            },
            error: (err) => {
              Swal.fire({
                icon: 'error',
                title: 'Oops...',
                text: 'Error al Guardar un Horario',
              });
            },
          });
      }
    }
  }

  goBack() {
    this.routerN.navigate(['admin/homeCompany'], {
      queryParams: { id: this.idCompany },
    });
  }
}
