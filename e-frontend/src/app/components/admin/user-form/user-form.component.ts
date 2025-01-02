import { Component } from '@angular/core';
import {
  FormBuilder,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Role, User, UserDto } from '../../../interfaces/interfaces';
import { CommonModule } from '@angular/common';
import { NavbarComponent } from '../../commons/navbar/navbar.component';
import { MatToolbarModule } from '@angular/material/toolbar';
import { AdminService } from '../../../services/admin.service';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-user-form',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    CommonModule,
    NavbarComponent,
    MatToolbarModule,
  ],
  templateUrl: './user-form.component.html',
  styleUrl: './user-form.component.scss',
})
export class UserFormComponent {
  userForm: FormGroup;
  isEditMode: boolean = false;
  userId: number | null = null;

  roles: Role[] = [];

  constructor(
    private fb: FormBuilder,
    private route: ActivatedRoute,
    private router: Router,
    private adminService: AdminService
  ) {
    this.userForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      username: ['', [Validators.required, Validators.minLength(3)]],
      password: ['', [Validators.required, Validators.minLength(6)]],
      idRole: [null, Validators.required],
    });
  }

  ngOnInit(): void {
    this.adminService.getRoles().subscribe({
      next: (value) => {
        this.roles = value;
      },
      error: (err) => {
        console.log(err);
      },
    });

    this.route.paramMap.subscribe((params) => {
      const id = params.get('id');
      if (id) {
        this.isEditMode = true;
        this.userId = +id;
        this.loadUser(this.userId);
      }
    });
  }

  loadUser(id: number) {
    this.adminService.getUserById(id).subscribe({
      next: (value) => {
        console.log(value);
      }, error: (err)=>  {
          console.log(err);
          
      },
    })
    // this.userForm.patchValue(existingUser);
    // this.userForm.get('password')?.setValidators(null);
    // this.userForm.get('password')?.updateValueAndValidity();
  }

  onSubmit(): void {
    if (this.userForm.valid) {
      const formValue = this.userForm.value;
      if (this.isEditMode) {
        console.log(
          'Guardar Cambios para el usuario con ID:',
          this.userId,
          formValue
        );
      } else {
        this.adminService.createUser(formValue).subscribe({
          next: (value) => {
            Swal.fire({
              position: 'top-end',
              icon: 'success',
              title: 'Usuario creado con exito',
              showConfirmButton: false,
              timer: 1500,
            });
          },
          error: (err) => {
            console.log(err);
          },
        });
      }
      setTimeout(() => {this.router.navigate(['/admin/users']);}, 1500)
    }
  }

  goBack() {
    this.router.navigate(['/admin/users']);
  }
}
