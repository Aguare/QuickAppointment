import { Component, Input } from '@angular/core';
import { Employee } from '../../../interfaces/interfaces';
import { CommonModule, DatePipe } from '@angular/common';
import { Router } from '@angular/router';
import Swal from 'sweetalert2';
import { AdminService } from '../../../services/admin.service';

@Component({
  selector: 'app-view-employees',
  standalone: true,
  imports: [CommonModule, DatePipe],
  templateUrl: './view-employees.component.html',
  styleUrl: './view-employees.component.scss',
})
export class ViewEmployeesComponent {
  @Input() employees: Employee[] = [];

  constructor(private router: Router, private adminService: AdminService) {}

  editEmployee(i: number) {
    const employee = this.employees[i];
    this.router.navigate([`admin/employees/edit/${employee.id}`]);
  }

  deleteEmployee(i: number) {
    const employee = this.employees[i];
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
        this.adminService.deleteEmployee(employee.id).subscribe({
          next: (value: any) => {
            Swal.fire({
              title: 'Deleted!',
              text: value.message,
              icon: 'success',
            });
            this.employees.splice(i, 1);
          },
          error: (err) => {
            console.log(err);
          },
        });
      }
    });
  }
}
