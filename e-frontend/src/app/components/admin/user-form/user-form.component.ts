import { Component } from '@angular/core';
import {
  FormBuilder,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { User } from '../../../interfaces/interfaces';
import { CommonModule } from '@angular/common';
import { NavbarComponent } from '../../commons/navbar/navbar.component';
import { MatToolbarModule } from '@angular/material/toolbar';

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

  roles = [
    { id: 1, name: 'Admin' },
    { id: 2, name: 'Editor' },
    { id: 3, name: 'Viewer' },
  ];

  constructor(
    private fb: FormBuilder,
    private route: ActivatedRoute,
    private router: Router
  ) {
    this.userForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      username: ['', [Validators.required, Validators.minLength(3)]],
      password: ['', [Validators.required, Validators.minLength(6)]],
      idRole: [null, Validators.required],
    });
  }

  ngOnInit(): void {
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
    const existingUser: User = {
      id: id,
      email: 'john@example.com',
      username: 'john_doe',
      isClient: false,
      password: '',
      idRole: 1,
      roleName: 'Admin',
    };
    this.userForm.patchValue(existingUser);
    this.userForm.get('password')?.setValidators(null);
    this.userForm.get('password')?.updateValueAndValidity();
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
        console.log('Guardar Nuevo Usuario:', formValue);
      }
      this.router.navigate(['/admin/users']);
    }
  }

  goBack() {
    this.router.navigate(['/admin/users']);
  }
}
