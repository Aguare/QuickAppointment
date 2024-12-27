import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import {
  FormBuilder,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Role } from '../../../interfaces/interfaces';
import { NavbarComponent } from '../../commons/navbar/navbar.component';
import { MatToolbarModule } from '@angular/material/toolbar';
import { AdminService } from '../../../services/admin.service';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-role-form',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    CommonModule,
    NavbarComponent,
    MatToolbarModule,
  ],
  templateUrl: './role-form.component.html',
  styleUrl: './role-form.component.scss',
})
export class RoleFormComponent {
  roleForm: FormGroup;
  isEditMode: boolean = false;
  roleId: number | null = null;

  constructor(
    private fb: FormBuilder,
    private route: ActivatedRoute,
    private router: Router,
    private adminService: AdminService
  ) {
    this.roleForm = this.fb.group({
      name: ['', [Validators.required, Validators.minLength(3)]],
      description: ['', [Validators.required, Validators.minLength(5)]],
      allowCreate: [false],
      allowEdit: [false],
      allowDelete: [false],
    });
  }

  ngOnInit(): void {
    this.route.paramMap.subscribe((params) => {
      const id = params.get('id');
      if (id) {
        this.isEditMode = true;
        this.roleId = +id;
        this.loadRole(this.roleId);
      }
    });
  }

  loadRole(id: number) {
    this.adminService.getRoleById(id).subscribe({
      next: (value) => {
        this.roleForm.patchValue(value);
      },
      error: (err) => {
        console.log(err);
      },
    });
  }

  onSubmit(): void {
    if (this.roleForm.valid) {
      const formValue = this.roleForm.value;
      if (this.isEditMode) {
        this.adminService.updateRole(this.roleId!, formValue).subscribe({
          next: (value: any) => {
            Swal.fire({
              position: "top-end",
              icon: "success",
              title: "Rol actualizado con exito",
              showConfirmButton: false,
              timer: 1500
            });
          },
          error: (err) => {
            console.log(err);
          },
        });
      } else {
        this.adminService.saveRole(formValue).subscribe({
          next: (value: any) => {
            Swal.fire({
              position: "top-end",
              icon: "success",
              title: "Rol guardado con exito",
              showConfirmButton: false,
              timer: 1500
            });
          },
          error: (err) => {
            console.log(err);
          },
        });
      }

      setTimeout(() => {this.router.navigate(['/admin/roles']);}, 1500)
    }
  }

  goBack() {
    this.router.navigate(['/admin/roles']);
  }
}
