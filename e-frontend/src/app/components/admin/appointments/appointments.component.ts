import { Component, OnInit } from '@angular/core';
import { NavbarComponent } from '../../commons/navbar/navbar.component';
import { MatToolbarModule } from '@angular/material/toolbar';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { AppointmentReport, Company } from '../../../interfaces/interfaces';
import { ClientService } from '../../../services/client.service';
import jsPDF from 'jspdf';
import autoTable from 'jspdf-autotable';
import { AdminService } from '../../../services/admin.service';

@Component({
  selector: 'app-appointments',
  standalone: true,
  imports: [NavbarComponent, MatToolbarModule, FormsModule, CommonModule],
  templateUrl: './appointments.component.html',
  styleUrl: './appointments.component.scss',
})
export class AppointmentsComponent implements OnInit {
  appointmens: AppointmentReport[] = [];
  companies: Company[] = [];

  showDateFilter = false;
  showBusinessFilter = false;
  showStatusFilter = false;

  startDate!: string;
  endDate!: string;
  selectedBusiness!: number;
  selectedStatus!: number;

  constructor(private clientService: ClientService, private adminService: AdminService) {}

  ngOnInit(): void {

    this.getAllAppointments();
    
    this.adminService.getCompanies().subscribe({
      next: (value: Company[]) => {
        this.companies = value
      },
      error: (err) => {},
    });
  }

  toggleDateFilter() {
    this.resetFilters();
    this.showDateFilter = !this.showDateFilter;
  }

  toggleBusinessFilter() {
    this.resetFilters();
    this.showBusinessFilter = !this.showBusinessFilter;
  }

  toggleStatusFilter() {
    this.resetFilters();
    this.showStatusFilter = !this.showStatusFilter;
  }

  toggleAllAppointments(){
    this.resetFilters();
    this.getAllAppointments();
  }

  resetFilters() {
    this.showDateFilter = false;
    this.showBusinessFilter = false;
    this.showStatusFilter = false;
  }

  getAllAppointments(){
    this.clientService.getAppointmentsReport().subscribe({
      next: (value: AppointmentReport[]) => {
        this.appointmens = value;
      },
      error: (err) => {
        console.log(err);
        
      },
    });
  }

  filterByDate() {
    this.clientService.getAppointmentsByDateReport(this.startDate, this.endDate).subscribe({
      next: (value: AppointmentReport[]) => {
        this.appointmens = value;
      },
      error: (err) => {
        console.log(err);
        
      },
    });
    
  }

  filterByBusiness() {

    this.clientService.getAppointmentsByCompanyReport(this.selectedBusiness).subscribe({
      next: (value: AppointmentReport[]) => {
        this.appointmens = value;
      },
      error: (err) => {
        console.log(err);
        
      },
    });
    
    
  }

  filterByStatus() {

    if(this.selectedStatus == 1){
      this.clientService.getAppointmentsByStatusReport(false).subscribe({
        next: (value: AppointmentReport[]) => {
          this.appointmens = value;
        },
        error: (err) => {
          console.log(err);
          
        },
      });
    } else if(this.selectedStatus == 2){
      this.clientService.getAppointmentsByStatusReport(true).subscribe({
        next: (value: AppointmentReport[]) => {
          this.appointmens = value;
        },
        error: (err) => {
          console.log(err);
          
        },
      });
    } else if(this.selectedStatus == 3){
      this.clientService.getAppointmentsCanceledReport(true).subscribe({
        next: (value: AppointmentReport[]) => {
          this.appointmens = value;
        },
        error: (err) => {
          console.log(err);
          
        },
      });
    }
  }

  exportToPDF() {
    const doc = new jsPDF();

    doc.text('Reporte de Citas', 14, 10);

    const columns = [
      'Username',
      'Email',
      'Fecha',
      'Hora',
      'Servicio',
      'Precio',
    ];
    const rows = this.appointmens.map((appointment) => [
      appointment.username,
      appointment.email,
      appointment.date,
      appointment.hour,
      appointment.service,
      appointment.price,
    ]);

    autoTable(doc, {
      head: [columns],
      body: rows,
      startY: 20,
    });

    const fileName = prompt('Ingrese el nombre del archivo:', 'Reporte_Citas');
    if (fileName) {
      doc.save(`${fileName}.pdf`);
    }
  }
}
