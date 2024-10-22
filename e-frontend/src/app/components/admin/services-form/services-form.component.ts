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
import { NavbarComponent } from '../../commons/navbar/navbar.component';
import { MatToolbarModule } from '@angular/material/toolbar';
import { AdminService } from '../../../services/admin.service';
import Swal from 'sweetalert2';
import { log } from 'console';

@Component({
  selector: 'app-services-form',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    CommonModule,
    NavbarComponent,
    MatToolbarModule,
  ],
  templateUrl: './services-form.component.html',
  styleUrl: './services-form.component.scss',
})
export class ServicesFormComponent {
  serviceForm: FormGroup;
  isEditMode: boolean = false;
  serviceId: number | null = null;
  idCompany: number | undefined;

  constructor(
    private fb: FormBuilder,
    private route: ActivatedRoute,
    private router: Router,
    private adminService: AdminService
  ) {
    this.serviceForm = this.fb.group({
      name: ['', [Validators.required, Validators.minLength(3)]],
      description: ['', [Validators.required, Validators.minLength(5)]],
      duration: [0, [Validators.required, Validators.min(1)]],
    });
  }

  ngOnInit(): void {
    this.route.queryParams.subscribe((params) => {
      this.idCompany = params['id'] ? +params['id'] : undefined;
    });

    this.route.paramMap.subscribe((params) => {
      const id = params.get('id');
      if (id) {
        this.isEditMode = true;
        this.serviceId = +id;
        this.loadService(this.serviceId);
      }
    });
  }

  loadService(id: number) {
    let existingService: Service | undefined;
    this.adminService.getTypeAppointemById(id).subscribe({
      next: (value: Service) => {
        existingService = value;
        this.idCompany = value.fkCompany;
        this.serviceForm.patchValue(existingService);
      },
      error: (err) => {
        console.log(err);
      },
    });
  }

  onSubmit(): void {
    if (this.serviceForm.valid) {
      const formValue = this.serviceForm.value;
      if (this.isEditMode) {
        this.adminService
          .updateTypeAppointment(this.serviceId!, formValue)
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

        this.adminService.saveTypeAppointment(body).subscribe({
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
    console.log('fff', this.idCompany);

    this.router.navigate(['admin/homeCompany'], {
      queryParams: { id: this.idCompany },
    });
  }
}
