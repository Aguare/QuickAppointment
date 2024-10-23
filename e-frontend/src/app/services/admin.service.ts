import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { File } from 'buffer';
import { Company, Employee, Service } from '../interfaces/interfaces';

@Injectable({
  providedIn: 'root',
})
export class AdminService {
  apiImage: string = 'http://localhost:8000/api/upload';
  apiCompany: string = 'http://localhost:8000/companies';
  apiTypeAppointment: string = 'http://localhost:8000/type-appointments';
  apiEmployee: string = 'http://localhost:8000/employees';

  constructor(private http: HttpClient) {}

  saveImage(file: any) {
    return this.http.post(this.apiImage, file);
  }

  saveCompany(body: any){
    return this.http.post(`${this.apiCompany}/create`, body);
  }
  
  getCompanies(){
    return this.http.get<Company[]>(`${this.apiCompany}/all`);
  }
  
  getCompanyById(id:number){
    return this.http.get<Company>(`${this.apiCompany}/${id}`);
  }
  
  updateNameDescCompany(id:number, body: any){
    return this.http.put(`${this.apiCompany}/update/${id}`,body);
  }
  
  updateLogoCompany(id:number, body: any){
    return this.http.put(`${this.apiCompany}/updateLogo/${id}`,body);
  }
  
  deleteCompany(id:number){
    return this.http.delete(`${this.apiCompany}/${id}`);
  }

  saveTypeAppointment(body: any){
    return this.http.post(`${this.apiTypeAppointment}/create`, body);
  }
  
  getTypeAppointemstCompany(fkCompany: number){
    return this.http.get<Service[]>(`${this.apiTypeAppointment}/company/${fkCompany}`);
  }
  
  getTypeAppointemById(id: number){
    return this.http.get<Service>(`${this.apiTypeAppointment}/${id}`);
  }
  
  updateTypeAppointment(id: number, body: any){
    return this.http.put(`${this.apiTypeAppointment}/update/${id}`, body);
  }
  
  deleteTypeAppointment(id: number){
    return this.http.delete(`${this.apiTypeAppointment}/${id}`);
  }

  saveEmployee(body: any){
    return this.http.post(`${this.apiEmployee}/create`, body);
  }

  getEmployeesCompany(fkCompany: number){
    return this.http.get<Employee[]>(`${this.apiEmployee}/company/${fkCompany}`);
  }

  getEmployeeById(id: number){
    return this.http.get<Employee>(`${this.apiEmployee}/${id}`);
  }

  updateEmployee(id: number, body: any){
    return this.http.put(`${this.apiEmployee}/update/${id}`, body);
  }

  deleteEmployee(id: number){
    return this.http.delete(`${this.apiEmployee}/${id}`);
  }
}
