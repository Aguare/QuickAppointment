import { Component, Input } from '@angular/core';
import { Service } from '../../../interfaces/interfaces';
import {
  FormBuilder,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { NavbarComponent } from "../../commons/navbar/navbar.component";
import { MatToolbarModule } from '@angular/material/toolbar';

@Component({
  selector: 'app-services-form',
  standalone: true,
  imports: [ReactiveFormsModule, CommonModule, NavbarComponent, MatToolbarModule],
  templateUrl: './services-form.component.html',
  styleUrl: './services-form.component.scss',
})
export class ServicesFormComponent {
  serviceForm: FormGroup;
  isEditMode: boolean = false;
  serviceId: number | null = null;
  idCompany: number | null = null;

  constructor(
    private fb: FormBuilder,
    private route: ActivatedRoute,
    private router: Router
  ) {
    this.serviceForm = this.fb.group({
      name: ['', [Validators.required, Validators.minLength(3)]],
      description: ['', [Validators.required, Validators.minLength(5)]],
      duration: [0, [Validators.required, Validators.min(1)]],
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
        this.serviceId = +id; // Convertimos el id a número
        this.loadService(this.serviceId); // Cargamos los datos del servicio
      }
    });
  }

  loadService(id: number) {

    const existingService: Service = {
      id: id,
      name: 'Service Name',
      description: 'Service Description',
      duration: 30,
      fk_company: 10
    };
    this.idCompany = existingService.fk_company;
    this.serviceForm.patchValue(existingService);
  }

  onSubmit(): void {
    if (this.serviceForm.valid) {
      const formValue = this.serviceForm.value;
      if (this.isEditMode) {
        console.log(
          'Guardar Cambios para el servicio con ID:',
          this.serviceId,
          formValue,
          this.idCompany
        );
      } else {
        console.log('Guardar Nuevo Servicio:', formValue, this.idCompany);
      }
      this.router.navigate(['admin/homeCompany'], { queryParams: { id: this.idCompany } });
    }
  }

  goBack(){
    this.router.navigate(['admin/homeCompany'], { queryParams: { id: this.idCompany } });
  }
}
