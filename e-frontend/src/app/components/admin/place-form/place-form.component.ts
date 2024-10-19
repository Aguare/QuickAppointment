import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Place } from '../../../interfaces/interfaces';
import { MatToolbar } from '@angular/material/toolbar';
import { NavbarComponent } from "../../commons/navbar/navbar.component";

@Component({
  selector: 'app-place-form',
  standalone: true,
  imports: [ReactiveFormsModule, CommonModule, MatToolbar, NavbarComponent],
  templateUrl: './place-form.component.html',
  styleUrl: './place-form.component.scss'
})
export class PlaceFormComponent implements OnInit {
  placeForm: FormGroup;
  isEditMode: boolean = false;
  placeId: number | null = null;
  idCompany: number | null = null;

  constructor(
    private fb: FormBuilder,
    private route: ActivatedRoute,
    private router: Router
  ) {
    this.placeForm = this.fb.group({
      name: ['', [Validators.required, Validators.minLength(3)]],
      place: ['', [Validators.required, Validators.minLength(2)]]
    });
  }

  ngOnInit(): void {
    
    this.route.queryParams.subscribe((params) => {
      this.idCompany = params['id'] ? +params['id'] : null;
      console.log('Company ID:', this.idCompany);
    });

    this.route.paramMap.subscribe(params => {
      const id = params.get('id');
      if (id) {
        this.isEditMode = true;
        this.placeId = +id;  
        this.loadPlace(this.placeId);  
      }
    });
  }

  loadPlace(id: number) {

    const existingPlace: Place = {
      id: id,
      name: 'Salon de Belleza',
      place: 'Centro Comercial',
      fk_company: 20
    };
    this.idCompany = existingPlace.fk_company;
    this.placeForm.patchValue(existingPlace);
  }

  onSubmit(): void {
    if (this.placeForm.valid) {
      const formValue = this.placeForm.value;
      if (this.isEditMode) {
        console.log('Guardar Cambios para el lugar con ID:', this.placeId, formValue);
        
      } else {
        console.log('Guardar Nuevo Lugar:', formValue);
        
      }
      
      this.router.navigate(['admin/homeCompany'], { queryParams: { id: this.idCompany } });
    }
  }

  goBack(){
    this.router.navigate(['admin/homeCompany'], { queryParams: { id: this.idCompany } });
  }
}
