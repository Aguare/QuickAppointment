import { Component, Input } from '@angular/core';
import { Employee } from '../../../interfaces/interfaces';
import { CommonModule, DatePipe } from '@angular/common';

@Component({
  selector: 'app-view-employees',
  standalone: true,
  imports: [CommonModule, DatePipe],
  templateUrl: './view-employees.component.html',
  styleUrl: './view-employees.component.scss'
})
export class ViewEmployeesComponent {
  @Input() employees: Employee[] = [];

  editEmployee(i: number){
    console.log('Editar empleado', i);
  }
  
  deleteEmployee(i: number){
    console.log('Eliminar servicio', i);
    this.employees.slice(i,1)
  }

}
