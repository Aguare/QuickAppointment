import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Page } from '../interfaces/interfaces';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  apiUser :string = 'http://localhost:8000/users';

  constructor(private http: HttpClient) { }

  register(body: any){
    return this.http.post(`${this.apiUser}/register`, body);
  }
  
  login(body: any){
    return this.http.post(`${this.apiUser}/login`, body);
  }

  getPages(idUser: number){    
    return this.http.get<Page[]>(`${this.apiUser}/pages/${idUser}`);
  }

}
