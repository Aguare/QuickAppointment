import { Component, OnInit } from '@angular/core';
import { NavbarComponent } from "../../commons/navbar/navbar.component";
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatCardModule } from '@angular/material/card';
import { CommonModule } from '@angular/common';
import { Company } from '../../../interfaces/interfaces';
import { ImagePipe } from '../../../pipes/image.pipe';
import { Router } from '@angular/router';
import { AdminService } from '../../../services/admin.service';

@Component({
  selector: 'app-client-home',
  standalone: true,
  imports: [NavbarComponent, MatToolbarModule, MatCardModule, CommonModule, ImagePipe],
  templateUrl: './client-home.component.html',
  styleUrl: './client-home.component.scss'
})
export class ClientHomeComponent implements OnInit{

  companies: Company[] = [];
  
  constructor(private router: Router, private adminService: AdminService){

  }

  ngOnInit(): void {
    this.adminService.getCompanies().subscribe({
      next: (value: Company[]) => {
        this.companies = value;
      },
      error: (err) => {},
    });
  }
  
  goToBusiness(id: number): void {
    this.router.navigate(['/client/company'], { queryParams: { id } });
  }

}
