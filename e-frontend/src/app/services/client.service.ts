import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Appointment, MyAppointment } from '../interfaces/interfaces';

@Injectable({
  providedIn: 'root'
})
export class ClientService {

  apiAppointments: string = 'http://localhost:8000/appointments';

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
  
  getMyAppointments(){
    return this.http.get<MyAppointment[]>(`${this.apiAppointments}/myReservations`);
  }

}
