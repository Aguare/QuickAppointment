import { Component, OnInit } from '@angular/core';
import {
  FormBuilder,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Employee } from '../../../interfaces/interfaces';
import { CommonModule } from '@angular/common';
import { MatToolbarModule } from '@angular/material/toolbar';
import { NavbarComponent } from '../../commons/navbar/navbar.component';
import { AdminService } from '../../../services/admin.service';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-employee-form',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    CommonModule,
    MatToolbarModule,
    NavbarComponent,
  ],
  templateUrl: './employee-form.component.html',
  styleUrl: './employee-form.component.scss',
})
export class EmployeeFormComponent implements OnInit {
  employeeForm: FormGroup;
  isEditMode: boolean = false;
  employeeId: number | null = null;
  idCompany: number | null = null;

  constructor(
    private fb: FormBuilder,
    private route: ActivatedRoute,
    private router: Router,
    private adminService: AdminService
  ) {
    // Inicializamos el formulario
    this.employeeForm = this.fb.group({
      first_name: ['', [Validators.required, Validators.minLength(2)]],
      last_name: ['', [Validators.required, Validators.minLength(2)]],
      dpi: ['', [Validators.required, Validators.pattern(/^[0-9]{13}$/)]],
      date_birth: ['', [Validators.required]],
    });
  }

  ngOnInit(): void {
    this.route.queryParams.subscribe((params) => {
      this.idCompany = params['id'] ? +params['id'] : null;
      console.log('Company ID:', this.idCompany);
    });

    this.route.paramMap.subscribe((params) => {
      const id = params.get('id');
      if (id) {
        this.isEditMode = true;
        this.employeeId = +id;
        this.loadEmployee(this.employeeId);
      }
    });
  }

  loadEmployee(id: number) {
    let existingEmploye: Employee | undefined;
    this.adminService.getEmployeeById(id).subscribe({
      next: (value: Employee) => {
        existingEmploye = value;
        this.idCompany = value.fkCompany;
        this.employeeForm.patchValue(existingEmploye);
      },
      error: (err) => {
        console.log(err);
      },
    });
  }

  onSubmit(): void {
    if (this.employeeForm.valid) {
      const formValue = this.employeeForm.value;
      if (this.isEditMode) {
        this.adminService
          .updateEmployee(this.employeeId!, formValue)
          .subscribe({
            next: (value: any) => {
              Swal.fire({
                position: 'top-end',
                icon: 'success',
                title: value.message,
                showConfirmButton: false,
                timer: 1500,
              });
            },
            error: (err) => {
              console.log(err);
            },
          });
      } else {
        const body = formValue;
        body.fkCompany = this.idCompany;
        console.log(body);

        this.adminService.saveEmployee(body).subscribe({
          next: (value: any) => {
            Swal.fire({
              position: 'top-end',
              icon: 'success',
              title: value.message,
              showConfirmButton: false,
              timer: 1500,
            });
          },
          error: (err) => {
            console.log(err);
          },
        });
      }
      setTimeout(() => {
        this.router.navigate(['admin/homeCompany'], {
          queryParams: { id: this.idCompany },
        });
      }, 1500);
    }
  }

  goBack() {
    this.router.navigate(['admin/homeCompany'], {
      queryParams: { id: this.idCompany },
    });
  }
}
