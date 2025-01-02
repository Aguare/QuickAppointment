import { Component, OnInit } from '@angular/core';
import { User, UserDto } from '../../../interfaces/interfaces';
import { CommonModule } from '@angular/common';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatIcon, MatIconModule } from '@angular/material/icon';
import { Router } from '@angular/router';
import { NavbarComponent } from "../../commons/navbar/navbar.component";
import Swal from 'sweetalert2';
import { AdminService } from '../../../services/admin.service';

@Component({
  selector: 'app-users',
  standalone: true,
  imports: [CommonModule, MatToolbarModule, MatIconModule, NavbarComponent],
  templateUrl: './users.component.html',
  styleUrl: './users.component.scss',
})
export class UsersComponent implements OnInit{
  
  users: UserDto[] = [];

  constructor(private router: Router, private adminService: AdminService){

  }

  ngOnInit(): void {
    this.adminService.getUsers().subscribe({
      next: (value) => {
        this.users = value;
      }, error: (err) => {
        console.log(err);
        
      },
    })
  }

  editUser(user: UserDto) {
    this.router.navigate([`/admin/users/edit/${user.id}`]);
  }

  deleteUser(user: UserDto) {
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
