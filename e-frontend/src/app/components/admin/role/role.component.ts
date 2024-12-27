import { Component, OnInit } from '@angular/core';
import { Role } from '../../../interfaces/interfaces';
import { NavbarComponent } from '../../commons/navbar/navbar.component';
import { CommonModule } from '@angular/common';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatIconModule } from '@angular/material/icon';
import { Router } from '@angular/router';
import Swal from 'sweetalert2';
import { AdminService } from '../../../services/admin.service';

@Component({
  selector: 'app-role',
  standalone: true,
  imports: [NavbarComponent, CommonModule, MatToolbarModule, MatIconModule],
  templateUrl: './role.component.html',
  styleUrl: './role.component.scss',
})
export class RoleComponent implements OnInit {
  roles: Role[] = [];

  constructor(private router: Router, private adminService: AdminService) {}

  ngOnInit(): void {
    this.adminService.getRoles().subscribe({
      next: (value: Role[]) => {
        this.roles = value.filter(rol => rol.id !== 1 && rol.id !== 2);
      },
      error: (err) => {
        console.log(err);
      },
    });
  }

  editRole(role: Role) {
    this.router.navigate([`/admin/roles/edit/${role.id}`]);
  }

  deleteRole(role: Role) {
    Swal.fire({
      title: '¿Estas seguro de querer eliminar este rol?',
      text: 'No podras recuperar los datos',
      icon: 'warning',
      showCancelButton: true,
      confirmButtonColor: '#3085d6',
      cancelButtonColor: '#d33',
      confirmButtonText: 'Borrar Rol',
    }).then((result) => {
      if (result.isConfirmed) {
        this.adminService.deleteRole(role.id).subscribe({
          next: (value: any) => {
            console.log(value);
            
            Swal.fire({
              title: 'Deleted!',
              text: 'El rol se elimino ',
              icon: 'success',
            });
          },
          error: (err) => {
            console.log(err);
          },
        });
      }
    });
  }

  assignRole(role: Role) {
    console.log('Asignar rol:', role);
  }

  addRole() {
    this.router.navigate(['/admin/roles/new']);
  }
}
