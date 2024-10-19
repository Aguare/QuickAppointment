import { Component, Input } from '@angular/core';
import { Employee } from '../../../interfaces/interfaces';
import { CommonModule, DatePipe } from '@angular/common';
import { Router } from '@angular/router';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-view-employees',
  standalone: true,
  imports: [CommonModule, DatePipe],
  templateUrl: './view-employees.component.html',
  styleUrl: './view-employees.component.scss'
})
export class ViewEmployeesComponent {
  
  @Input() employees: Employee[] = [];

  constructor(private router:Router){}

  editEmployee(i: number){
    this.router.navigate([`admin/employees/edit/${i}`])
  }
  
  deleteEmployee(i: number){

    const employee = this.employees[i]
    Swal.fire({
      title: '¿Estas seguro de querer eliminar este empleado?',
      text: 'No podras recuperar los datos',
      icon: 'warning',
      showCancelButton: true,
      confirmButtonColor: '#3085d6',
      cancelButtonColor: '#d33',
      confirmButtonText: 'Borrar Empleado',
    }).then((result) => {
      if (result.isConfirmed) {
        Swal.fire({
          title: 'Deleted!',
          text: 'El empleado se elimino ' + employee.first_name,
          icon: 'success',
        });
      }
    });
  }

}
