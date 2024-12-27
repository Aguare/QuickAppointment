import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import {
  Appointment,
  AppointmentReport,
  Billing,
  MyAppointment,
} from '../interfaces/interfaces';

@Injectable({
  providedIn: 'root',
})
export class ClientService {
  apiAppointments: string = 'http://localhost:8000/appointments';
  apiBilling: string = 'http://localhost:8000/billing';

  constructor(private http: HttpClient) {}

  saveAppointment(body: any) {
    return this.http.post(`${this.apiAppointments}/create`, body);
  }

  getAppointmetsByUser(fkUser: number) {
    return this.http.get<Appointment[]>(
      `${this.apiAppointments}/user/${fkUser}`
    );
  }

  getAppointmetsByDate(date: string) {
    return this.http.get<Appointment[]>(`${this.apiAppointments}/date/${date}`);
  }

  getAppointmentById(id: number) {
    return this.http.get<Appointment>(`${this.apiAppointments}/${id}`);
  }

  updateAppointment(id: number, body: any) {
    return this.http.put(`${this.apiAppointments}/update/${id}`, body);
  }

  cancelAppointment(id: number) {
    return this.http.put(`${this.apiAppointments}/cancel/${id}`, null);
  }

  getMyAppointments(fkUser: number) {
    return this.http.get<MyAppointment[]>(
      `${this.apiAppointments}/myReservations/${fkUser}`
    );
  }

  getAppointmentsByYear() {
    return this.http.get(`${this.apiAppointments}/appointmentByYear`);
  }

  saveBilling(body: any) {
    return this.http.post(`${this.apiBilling}/create`, body);
  }

  getBillingsByUser(fkUser: number) {
    return this.http.get<Billing>(`${this.apiBilling}/user/${fkUser}`);
  }

  updateBilling(id: number, body: any) {
    return this.http.put(`${this.apiBilling}/update/${id}`, body);
  }

  getAppointmentsReport() {
    return this.http.get<AppointmentReport[]>(
      `${this.apiAppointments}/appointmentReport`
    );
  }

  getAppointmentsByCompanyReport(fkCompany: number) {
    return this.http.get<AppointmentReport[]>(
      `${this.apiAppointments}/appointmentsCompany/${fkCompany}`
    );
  }

  getAppointmentsByStatusReport(status: boolean) {
    return this.http.get<AppointmentReport[]>(
      `${this.apiAppointments}/appointmentsStatus/${status}`
    );
  }

  getAppointmentsCanceledReport(isCanceled: boolean) {
    return this.http.get<AppointmentReport[]>(
      `${this.apiAppointments}/appointmentsCanceled/${isCanceled}`
    );
  }

  getAppointmentsByDateReport(startDate: string, endDate: string) {
    return this.http.get<AppointmentReport[]>(
      `${this.apiAppointments}/appointmentsByDate/${startDate}/${endDate}`
    );
  }

  getAppointmentsByCompany() {
    return this.http.get(`${this.apiAppointments}/appointmentByCompanyReport`);
  }
}
