import { CommonModule } from '@angular/common';
import { Component, Input } from '@angular/core';
import { Place } from '../../../interfaces/interfaces';

@Component({
  selector: 'app-view-places',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './view-places.component.html',
  styleUrl: './view-places.component.scss'
})
export class ViewPlacesComponent {

  @Input() places: Place[] = [];

  editPlace(i: number){
    console.log('Editar lugar', i);
  }
  
  deletePlace(i: number){
    console.log('Eliminar lugar', i);
  }

}
