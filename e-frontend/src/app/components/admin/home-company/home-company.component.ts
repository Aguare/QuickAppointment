import { Component } from '@angular/core';
import { MatIconModule } from '@angular/material/icon';
import { ActivatedRoute, Router } from '@angular/router';
import { NavbarComponent } from '../../commons/navbar/navbar.component';
import { EditCompanySettingsComponent } from '../edit-company-settings/edit-company-settings.component';
import { MatDialog } from '@angular/material/dialog';
import Swal from 'sweetalert2';
import { BannerComponent } from '../../commons/banner/banner.component';
import { MatTabsModule } from '@angular/material/tabs';
import { MatTableModule } from '@angular/material/table';
import {
  Company,
  Employee,
  Place,
  Service,
} from '../../../interfaces/interfaces';
import { employees, places } from '../../../db/db';
import { CommonModule, DatePipe } from '@angular/common';
import { ViewServicesComponent } from '../view-services/view-services.component';
import { ViewEmployeesComponent } from '../view-employees/view-employees.component';
import { ViewPlacesComponent } from '../view-places/view-places.component';
import { MatToolbarModule } from '@angular/material/toolbar';
import { LocalStorageService } from '../../../services/local-storage.service';
import { AdminService } from '../../../services/admin.service';
import { ImagePipe } from '../../../pipes/image.pipe';

@Component({
  selector: 'app-home-company',
  standalone: true,
  imports: [
    MatIconModule,
    NavbarComponent,
    BannerComponent,
    MatTabsModule,
    MatTableModule,
    DatePipe,
    CommonModule,
    ViewServicesComponent,
    ViewEmployeesComponent,
    ViewPlacesComponent,
    MatToolbarModule,
    ImagePipe,
  ],
  templateUrl: './home-company.component.html',
  styleUrl: './home-company.component.scss',
})
export class HomeCompanyComponent {
  idCompany: number | null = null;
  services: Service[] = [];
  places: Place[] = [];
  employees: Employee[] = [];
  selectedTabIndex: number = 0;
  company: Company = {
    id: 2,
    name: 'Nombre del Negocio',
    description:
      'Esta es la descripción de tu negocio. Aquí puedes hablar sobre lo que ofreces.',
    logo: 'https://marketplace.canva.com/EAGD9V5Eoeg/1/0/1600w/canva-logo-club-de-futbol-sencillo-deportivo-azul-CwnW56rMPJ8.jpg',
  };

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    public dialog: MatDialog,
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
    }

    const savedTabIndex = this.localStorageService.getSelectedTabIndex();
    if (savedTabIndex !== null) {
      this.selectedTabIndex = +savedTabIndex;
    }

    if (this.idCompany) {
      this.adminService.getTypeAppointemstCompany(this.idCompany).subscribe({
        next: (value: Service[]) => {
          this.services = value;
        },
        error: (err) => {
          console.log(err);
        },
      });
      this.adminService.getEmployeesCompany(this.idCompany).subscribe({
        next: (value: Employee[]) => {
          this.employees = value;
        },
        error: (err) => {
          console.log(err);
        },
      });
    }
  }

  onTabChange(index: number) {
    this.selectedTabIndex = index;
    this.localStorageService.addSelectedTabIndex(this.selectedTabIndex);
  }

  editCompany(): void {
    const dialogRef = this.dialog.open(EditCompanySettingsComponent, {
      width: '800px',
      data: {
        name: this.company.name,
        description: this.company.description,
      },
    });

    dialogRef.afterClosed().subscribe((result) => {
      if (result.logo) {
        const formData: FormData = new FormData();
        formData.append('file', result.logo);
        this.adminService.saveImage(formData).subscribe({
          next: (resp: any) => {
            console.log(resp.relativePath);
            const body = {
              oldPath: this.company.logo,
              newPath: resp.relativePath,
            };
            this.adminService
              .updateLogoCompany(this.idCompany!, body)
              .subscribe({
                next: (value: any) => {
                  Swal.fire({
                    position: 'top-end',
                    icon: 'success',
                    title: value.message,
                    showConfirmButton: false,
                    timer: 1500,
                  });

                  setTimeout(() => {
                    window.location.reload();
                  }, 1500);
                },
                error: (err) => {
                  console.log(err);
                },
              });
          },
          error: (error: any) => {
            console.log(error);
          },
        });
      } else {
        const body = {
          name: result.name,
          description: result.description,
        };

        this.adminService
          .updateNameDescCompany(this.idCompany!, body)
          .subscribe({
            next: (value: any) => {
              Swal.fire({
                position: 'top-end',
                icon: 'success',
                title: value.message,
                showConfirmButton: false,
                timer: 1500,
              });

              setTimeout(() => {
                window.location.reload();
              }, 1500);
            },
            error: (err) => {
              console.log(err);
            },
          });
      }
    });
  }

  deleteCompany(): void {
    Swal.fire({
      title: '¿Estas seguro de querer eliminar este negocio?',
      text: 'Se perderan todos los servicios, personal y lugares.',
      icon: 'warning',
      showCancelButton: true,
      confirmButtonColor: '#3085d6',
      cancelButtonColor: '#d33',
      confirmButtonText: 'Borrar Negocio',
    }).then((result) => {
      if (result.isConfirmed) {
        this.adminService.deleteCompany(this.idCompany!).subscribe({
          next: (value: any) => {
            Swal.fire({
              position: 'top-end',
              icon: 'success',
              title: value.message,
              showConfirmButton: false,
              timer: 1500,
            });

            setTimeout(() => {
              this.router.navigate(['admin/init']);
            }, 1500);
          },
          error: (err) => {
            console.log(err);
          },
        });
      }
    });
  }

  addService() {
    this.router.navigate(['admin/services/new'], {
      queryParams: { id: this.idCompany },
    });
  }
  addEmployees() {
    this.router.navigate(['admin/employees/new'], {
      queryParams: { id: this.idCompany },
    });
  }
  addPlaces() {
    this.router.navigate(['admin/places/new'], {
      queryParams: { id: this.idCompany },
    });
  }
}
