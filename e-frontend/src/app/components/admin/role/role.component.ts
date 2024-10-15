import { Component } from '@angular/core';
import { Role } from '../../../interfaces/interfaces';
import { NavbarComponent } from '../../commons/navbar/navbar.component';
import { CommonModule } from '@angular/common';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatIconModule } from '@angular/material/icon';
import { Router } from '@angular/router';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-role',
  standalone: true,
  imports: [NavbarComponent, CommonModule, MatToolbarModule, MatIconModule],
  templateUrl: './role.component.html',
  styleUrl: './role.component.scss',
})
export class RoleComponent {

  constructor(private router: Router){}

  roles: Role[] = [
    {
      id: 1,
      name: 'Administrador',
      description: 'Acceso completo al sistema',
      allowCreate: true,
      allowEdit: true,
      allowDelete: true,
    },
    {
      id: 2,
      name: 'Editor',
      description: 'Permisos de edición de contenido',
      allowCreate: true,
      allowEdit: true,
      allowDelete: false,
    },
    {
      id: 3,
      name: 'Lector',
      description: 'Solo puede leer el contenido',
      allowCreate: false,
      allowEdit: false,
      allowDelete: false,
    },
  ];

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
        Swal.fire({
          title: 'Deleted!',
          text: 'El rol se elimino ',
          icon: 'success',
        });
      }
    });
  }

  assignRole(role: Role) {
    console.log('Asignar rol:', role);
  }

  addRole(){
    this.router.navigate(['/admin/roles/new']);
  }
}
