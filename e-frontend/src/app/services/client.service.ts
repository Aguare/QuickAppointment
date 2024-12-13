import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Appointment, Billing, MyAppointment } from '../interfaces/interfaces';
import { log } from 'node:console';

@Injectable({
  providedIn: 'root'
})
export class ClientService {

  apiAppointments: string = 'http://localhost:8000/appointments';
  apiBilling: string = 'http://localhost:8000/billing';

  constructor(private http: HttpClient) { }

  saveAppointment(body: any){
    return this.http.post(`${this.apiAppointments}/create`, body);
  }

  getAppointmetsByUser(fkUser: number){
    return this.http.get<Appointment[]>(`${this.apiAppointments}/user/${fkUser}`);
  }
  
  getAppointmetsByDate(date: string){
    return this.http.get<Appointment[]>(`${this.apiAppointments}/date/${date}`);
  }
  
  getMyAppointments(fkUser: number){
    return this.http.get<MyAppointment[]>(`${this.apiAppointments}/myReservations/${fkUser}`);
  }
  
  getAppointmentsByYear(){
    return this.http.get(`${this.apiAppointments}/appointmentByYear`);
  }

  saveBilling(body: any){
    return this.http.post(`${this.apiBilling}/create`, body);
  }

  getBillingsByUser(fkUser: number){
    return this.http.get<Billing>(`${this.apiBilling}/user/${fkUser}`);
  }

  updateBilling(id:number, body: any){
    return this.http.put(`${this.apiBilling}/update/${id}`,body);
  }

}
