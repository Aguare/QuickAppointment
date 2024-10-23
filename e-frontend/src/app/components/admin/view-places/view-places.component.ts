import { CommonModule } from '@angular/common';
import { Component, Input } from '@angular/core';
import { Place } from '../../../interfaces/interfaces';
import { Router } from '@angular/router';
import Swal from 'sweetalert2';
import { AdminService } from '../../../services/admin.service';

@Component({
  selector: 'app-view-places',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './view-places.component.html',
  styleUrl: './view-places.component.scss',
})
export class ViewPlacesComponent {
  constructor(private router: Router, private adminService: AdminService) {}

  @Input() places: Place[] = [];

  editPlace(i: number) {
    const place = this.places[i];
    this.router.navigate([`admin/places/edit/${place.id}`]);
  }

  deletePlace(i: number) {
    const place = this.places[i];
    Swal.fire({
      title: '¿Estas seguro de querer eliminar este lugar?',
      text: 'No podras recuperar los datos',
      icon: 'warning',
      showCancelButton: true,
      confirmButtonColor: '#3085d6',
      cancelButtonColor: '#d33',
      confirmButtonText: 'Borrar Lugar',
    }).then((result) => {
      if (result.isConfirmed) {
        this.adminService.deletePlace(place.id).subscribe({
          next: (value: any) => {
            Swal.fire({
              title: 'Deleted!',
              text: value.message,
              icon: 'success',
            });
            this.places.splice(i, 1);
          },
          error: (err) => {
            console.log(err);
          },
        });
      }
    });
  }
}
