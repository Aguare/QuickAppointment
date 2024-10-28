import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';

import { NavbarComponent } from '../../commons/navbar/navbar.component';
import { Company } from '../../../interfaces/interfaces';

import { MatCardModule } from '@angular/material/card';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatIconModule } from '@angular/material/icon';
import { AdminService } from '../../../services/admin.service';
import { ImagePipe } from '../../../pipes/image.pipe';

@Component({
  selector: 'app-admin-home',
  standalone: true,
  imports: [
    CommonModule,
    NavbarComponent,
    MatCardModule,
    MatToolbarModule,
    MatIconModule,
    ImagePipe
  ],
  templateUrl: './admin-home.component.html',
  styleUrl: './admin-home.component.scss',
})
export class AdminHomeComponent implements OnInit {
  companies: Company[] = [];

  constructor(private router: Router, private adminService: AdminService) {}

  ngOnInit(): void {
    this.adminService.getCompanies().subscribe({
      next: (value: Company[]) => {
        this.companies = value;
      },
      error: (err) => {},
    });
  }

  goToBusiness(id: number): void {
    this.router.navigate(['/admin/homeCompany'], { queryParams: { id } });
  }

  addCompany(): void {
    this.router.navigate(['/admin/addCompany']);
  }
}
