import { Component } from '@angular/core';
import { MatIconModule } from '@angular/material/icon';
import { ActivatedRoute, Router } from '@angular/router';
import { NavbarComponent } from '../../commons/navbar/navbar.component';
import { EditCompanySettingsComponent } from '../edit-company-settings/edit-company-settings.component';
import { MatDialog } from '@angular/material/dialog';
import Swal from 'sweetalert2';
import { BannerComponent } from "../../commons/banner/banner.component";

@Component({
  selector: 'app-home-company',
  standalone: true,
  imports: [MatIconModule, NavbarComponent, BannerComponent],
  templateUrl: './home-company.component.html',
  styleUrl: './home-company.component.scss',
})
export class HomeCompanyComponent {
  idCompany: number | null = null;
  business = {
    name: 'Nombre del Negocio',
    description:
      'Esta es la descripción de tu negocio. Aquí puedes hablar sobre lo que ofreces.',
    logo: 'https://marketplace.canva.com/EAGD9V5Eoeg/1/0/1600w/canva-logo-club-de-futbol-sencillo-deportivo-azul-CwnW56rMPJ8.jpg',
  };

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    public dialog: MatDialog
  ) {}

  ngOnInit(): void {
    this.route.queryParams.subscribe((params) => {
      this.idCompany = params['id'] ? +params['id'] : null;
      console.log('Company ID:', this.idCompany);
    });
  }

  editCompany(): void {
    const dialogRef = this.dialog.open(EditCompanySettingsComponent, {
      width: '800px',
      data: {
        name: this.business.name,
        description: this.business.description,
      },
    });

    dialogRef.afterClosed().subscribe((result) => {
      if (result) {
        console.log('Datos actualizados:', result);
        this.business.name = result.name;
        this.business.description = result.description;
        if (result.logo) {
          const reader = new FileReader();
          reader.onload = () => {
            const logo = reader.result;
            if(typeof(logo) === 'string'){
              this.business.logo = logo;
            }
          };
          reader.readAsDataURL(result.logo);
        }
      }
    });
  }

  deleteCompany(): void {
    Swal.fire({
      title: "¿Estas seguro de querer eliminar este negocio?",
      text: "Se perderan todos los servicios, personal y lugares.",
      icon: "warning",
      showCancelButton: true,
      confirmButtonColor: "#3085d6",
      cancelButtonColor: "#d33",
      confirmButtonText: "Borrar Negocio"
    }).then((result) => {
      if (result.isConfirmed) {
        Swal.fire({
          title: "Deleted!",
          text: "El servicio de elimino",
          icon: "success"
        });
      }
    });
  }
}
