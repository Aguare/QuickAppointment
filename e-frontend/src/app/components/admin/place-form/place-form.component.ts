import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import {
  FormBuilder,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Place } from '../../../interfaces/interfaces';
import { MatToolbar } from '@angular/material/toolbar';
import { NavbarComponent } from '../../commons/navbar/navbar.component';
import { AdminService } from '../../../services/admin.service';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-place-form',
  standalone: true,
  imports: [ReactiveFormsModule, CommonModule, MatToolbar, NavbarComponent],
  templateUrl: './place-form.component.html',
  styleUrl: './place-form.component.scss',
})
export class PlaceFormComponent implements OnInit {
  placeForm: FormGroup;
  isEditMode: boolean = false;
  placeId: number | null = null;
  idCompany: number | null = null;

  constructor(
    private fb: FormBuilder,
    private route: ActivatedRoute,
    private router: Router,
    private adminService: AdminService
  ) {
    this.placeForm = this.fb.group({
      name: ['', [Validators.required, Validators.minLength(3)]],
      place: ['', [Validators.required, Validators.minLength(2)]],
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
        this.placeId = +id;
        this.loadPlace(this.placeId);
      }
    });
  }

  loadPlace(id: number) {
    let existingPlace: Place | undefined;
    this.adminService.getPlaceById(id).subscribe({
      next: (value: Place) => {
        existingPlace = value;
        this.idCompany = value.fkCompany;
        this.placeForm.patchValue(existingPlace);
      },
      error: (err) => {
        console.log(err);
      },
    });
  }

  onSubmit(): void {
    if (this.placeForm.valid) {
      const formValue = this.placeForm.value;
      if (this.isEditMode) {
        this.adminService.updatePlace(this.placeId!, formValue).subscribe({
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

        this.adminService.savePlace(body).subscribe({
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
