import { CommonModule } from '@angular/common';
import { Component, Input } from '@angular/core';
import { Place } from '../../../interfaces/interfaces';
import { Router } from '@angular/router';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-view-places',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './view-places.component.html',
  styleUrl: './view-places.component.scss'
})
export class ViewPlacesComponent {

  constructor(private router: Router){}

  @Input() places: Place[] = [];

  editPlace(i: number){
    this.router.navigate([`admin/places/edit/${i}`])
  }
  
  deletePlace(i: number){

    const place = this.places[i]
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
        Swal.fire({
          title: 'Deleted!',
          text: 'El lugar se elimino ' + place.name,
          icon: 'success',
        });
      }
    });
  }

}
