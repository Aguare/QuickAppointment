import { Component } from '@angular/core';
import { NavbarComponent } from '../../commons/navbar/navbar.component';
import { BannerComponent } from '../../commons/banner/banner.component';
import { ActivatedRoute, Router } from '@angular/router';
import { AdminService } from '../../../services/admin.service';
import { LocalStorageService } from '../../../services/local-storage.service';
import { Company, Service } from '../../../interfaces/interfaces';
import { ImagePipe } from '../../../pipes/image.pipe';
import { CommonModule, CurrencyPipe } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';

@Component({
  selector: 'app-company',
  standalone: true,
  imports: [
    NavbarComponent,
    BannerComponent,
    ImagePipe,
    CurrencyPipe,
    CommonModule,
    MatCardModule,
    MatButtonModule
  ],
  templateUrl: './company.component.html',
  styleUrl: './company.component.scss',
})
export class CompanyComponent {
  services: Service[] = [];

  idCompany: number | null = null;
  company: Company = {
    id: 2,
    name: 'Nombre del Negocio',
    description:
      'Esta es la descripción de tu negocio. Aquí puedes hablar sobre lo que ofreces.',
    logo: 'https://marketplace.canva.com/EAGD9V5Eoeg/1/0/1600w/canva-logo-club-de-futbol-sencillo-deportivo-azul-CwnW56rMPJ8.jpg',
    courtRental: false
  };

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private localStorageService: LocalStorageService,
    private adminService: AdminService
  ) {}

  ngOnInit(): void {
    this.route.queryParams.subscribe((params) => {
      this.idCompany = params['id'] ? +params['id'] : null;
    });

    if (this.idCompany) {
      this.adminService.getCompanyById(this.idCompany).subscribe({
        next: (value: Company) => {
          this.company = value;
        },
        error: (err) => {
          console.log(err);
        },
      });

      this.adminService.getTypeAppointemstCompany(this.idCompany).subscribe({
        next: (value: Service[]) => {
          this.services = value;
        },
        error: (err) => {
          console.log(err);
        },
      });
    }
  }
}
