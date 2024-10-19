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
import { NavbarComponent } from "../../commons/navbar/navbar.component";
import { MatToolbarModule } from '@angular/material/toolbar';

@Component({
  selector: 'app-role-form',
  standalone: true,
  imports: [ReactiveFormsModule, CommonModule, NavbarComponent, MatToolbarModule],
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
    private router: Router
  ) {
    // Inicializamos el formulario
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
    const existingRole: Role = {
      id: id,
      name: 'Administrador',
      description: 'Rol con acceso completo al sistema',
      allowCreate: true,
      allowEdit: true,
      allowDelete: false,
    };
    this.roleForm.patchValue(existingRole);
  }

  onSubmit(): void {
    if (this.roleForm.valid) {
      const formValue = this.roleForm.value;
      if (this.isEditMode) {
        console.log(
          'Guardar Cambios para el rol con ID:',
          this.roleId,
          formValue
        );
        // Aquí va la lógica para actualizar el rol en el backend
      } else {
        console.log('Guardar Nuevo Rol:', formValue);
        // Aquí va la lógica para guardar un nuevo rol en el backend
      }
      // Redirigir a otra página después de guardar (opcional)
      this.router.navigate(['/admin/roles']);
    }
  }

  goBack(){
    this.router.navigate(['/admin/roles']);
  }
}
