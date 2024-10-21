import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { File } from 'buffer';
import { Company } from '../interfaces/interfaces';

@Injectable({
  providedIn: 'root',
})
export class AdminService {
  apiImage: string = 'http://localhost:8000/api/upload';
  apiCompany: string = 'http://localhost:8000/companies';

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
}