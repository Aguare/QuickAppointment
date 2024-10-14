import { CommonModule } from '@angular/common';
import { Component, Input } from '@angular/core';
import { Service } from '../../../interfaces/interfaces';

@Component({
  selector: 'app-view-services',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './view-services.component.html',
  styleUrl: './view-services.component.scss'
})
export class ViewServicesComponent {

    @Input() services: Service[] = [];

    editService(i: number){
      console.log('Editar servicio', i);
    }
    
    deleteService(i: number){
      console.log('Eliminar servicio', i);
    }

}
