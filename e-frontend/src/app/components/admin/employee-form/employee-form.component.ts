import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Employee } from '../../../interfaces/interfaces';
import { CommonModule } from '@angular/common';
import { MatToolbarModule } from '@angular/material/toolbar';
import { NavbarComponent } from "../../commons/navbar/navbar.component";

@Component({
  selector: 'app-employee-form',
  standalone: true,
  imports: [ReactiveFormsModule, CommonModule, MatToolbarModule, NavbarComponent],
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
    private router: Router
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
    const existingEmployee: Employee = {
      id: id,
      first_name: 'John',
      last_name: 'Doe',
      dpi: '1234567890123',
      date_birth: new Date('1990-01-01'),
      fk_company: 15,
    };
    this.idCompany = existingEmployee.fk_company;
    this.employeeForm.patchValue(existingEmployee);
  }

  onSubmit(): void {
    if (this.employeeForm.valid) {
      const formValue = this.employeeForm.value;
      if (this.isEditMode) {
        console.log(
          'Guardar Cambios para el empleado con ID:',
          this.employeeId,
          formValue
        );
      } else {
        console.log('Guardar Nuevo Empleado:', formValue);
      }

      this.router.navigate(['admin/homeCompany'], {
        queryParams: { id: this.idCompany },
      });
    }
  }

  goBack() {
    
    this.router.navigate(['admin/homeCompany'], {
      queryParams: { id: this.idCompany },
    });
  }
}
