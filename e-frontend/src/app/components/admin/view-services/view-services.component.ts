import { CommonModule } from '@angular/common';
import { Component, Input } from '@angular/core';
import { Service } from '../../../interfaces/interfaces';
import { Router } from '@angular/router';
import Swal from 'sweetalert2';
import { AdminService } from '../../../services/admin.service';

@Component({
  selector: 'app-view-services',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './view-services.component.html',
  styleUrl: './view-services.component.scss',
})
export class ViewServicesComponent {
  constructor(private router: Router, private adminService: AdminService) {}

  @Input() services: Service[] = [];

  editService(i: number) {
    const service = this.services[i];
    this.router.navigate([`admin/services/edit/${service.id}`]);
  }

  deleteService(i: number) {
    const service = this.services[i];
    Swal.fire({
      title: '¿Estas seguro de querer eliminar este servicio?',
      text: 'No podras recuperar los datos',
      icon: 'warning',
      showCancelButton: true,
      confirmButtonColor: '#3085d6',
      cancelButtonColor: '#d33',
      confirmButtonText: 'Borrar Servicio',
    }).then((result) => {
      if (result.isConfirmed) {
        this.adminService.deleteTypeAppointment(service.id).subscribe({
          next: (value: any) => {
            Swal.fire({
              title: 'Deleted!',
              text: value.message,
              icon: 'success',
            });
            this.services.slice(i, 1);
          },
          error: (err) => {
            console.log(err);
          },
        });
      }
    });
  }
}
