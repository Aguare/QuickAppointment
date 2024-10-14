import { CommonModule } from '@angular/common';
import { Component, Input } from '@angular/core';
import { Service } from '../../../interfaces/interfaces';
import { Router } from '@angular/router';

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
      console.log('Eliminar servicio', i);
    }

}
