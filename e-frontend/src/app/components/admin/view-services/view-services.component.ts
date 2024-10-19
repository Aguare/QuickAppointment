import { CommonModule } from '@angular/common';
import { Component, Input } from '@angular/core';
import { Service } from '../../../interfaces/interfaces';
import { Router } from '@angular/router';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-view-services',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './view-services.component.html',
  styleUrl: './view-services.component.scss'
})
export class ViewServicesComponent {

    constructor(private router: Router){}

    @Input() services: Service[] = [];

    editService(i: number){
      console.log('Editar servicio', i);
      this.router.navigate([`admin/services/edit/${i}`])
    }
    
    deleteService(i: number){

      const service = this.services[i]
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
          Swal.fire({
            title: 'Deleted!',
            text: 'El servicio se elimino ' + service.name,
            icon: 'success',
          });
        }
      });
    }

}
