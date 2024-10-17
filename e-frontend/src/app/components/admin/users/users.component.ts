import { Component } from '@angular/core';
import { User } from '../../../interfaces/interfaces';
import { CommonModule } from '@angular/common';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatIcon, MatIconModule } from '@angular/material/icon';
import { Router } from '@angular/router';
import { NavbarComponent } from "../../commons/navbar/navbar.component";
import Swal from 'sweetalert2';

@Component({
  selector: 'app-users',
  standalone: true,
  imports: [CommonModule, MatToolbarModule, MatIconModule, NavbarComponent],
  templateUrl: './users.component.html',
  styleUrl: './users.component.scss',
})
export class UsersComponent {
  
  users: User[] = [
    {
      id: 1,
      email: 'john@example.com',
      username: 'john_doe',
      isClient: false,
      idRole: 1,
      roleName: 'Admin',
    },
    {
      id: 2,
      email: 'jane@example.com',
      username: 'jane_smith',
      isClient: true,
      idRole: 2,
      roleName: 'Editor',
    },
    {
      id: 3,
      email: 'paul@example.com',
      username: 'paul_brown',
      isClient: true,
      idRole: 3,
      roleName: 'Viewer',
    },
  ];

  constructor(private router: Router){

  }

  editUser(user: User) {
    this.router.navigate([`/admin/users/edit/${user.id}`]);
  }

  deleteUser(user: User) {
    Swal.fire({
      title: '¿Estas seguro de querer eliminar este usuario?',
      text: 'No podras recuperar los datos',
      icon: 'warning',
      showCancelButton: true,
      confirmButtonColor: '#3085d6',
      cancelButtonColor: '#d33',
      confirmButtonText: 'Borrar Usuario',
    }).then((result) => {
      if (result.isConfirmed) {
        Swal.fire({
          title: 'Deleted!',
          text: 'El usuario se elimino ',
          icon: 'success',
        });
      }
    });
  }

  addUser(){
    this.router.navigate(['/admin/users/new']);
  }
}
