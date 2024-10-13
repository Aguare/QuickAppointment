import { Component } from '@angular/core';
import { NavbarComponent } from "../../commons/navbar/navbar.component";
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { MatToolbarModule } from '@angular/material/toolbar';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-add-company',
  standalone: true,
  imports: [NavbarComponent, ReactiveFormsModule, CommonModule, MatToolbarModule],
  templateUrl: './add-company.component.html',
  styleUrl: './add-company.component.scss'
})
export class AddCompanyComponent {
  companyForm: FormGroup;
  logoPreview: string | ArrayBuffer | null = null;
  logo: File | null = null;
  fileName: string = '';

  constructor(private fb: FormBuilder, private router: Router) {
    this.companyForm = this.fb.group({
      name: ['', [Validators.required, Validators.maxLength(30)]],
      description: ['', [Validators.required]]
    });
  }

  onSubmit() {
    if (this.companyForm.valid) {
      if(this.logo){
        const body = this.companyForm.value;
        body.logo = this.logo;
        console.log(body);
        
      } else {
        Swal.fire('Debes seleccionar una imagen para el logo')
      }
    }
  }

  onLogoSelected(event: any) {
    const file = event.target.files[0];
    
    if (file) {
      const reader = new FileReader();
      reader.onload = () => {
        this.logoPreview = reader.result;
        this.logo = file;
        this.fileName = file.name
      };
      reader.readAsDataURL(file);
    }
  }

  goBack() {
    this.router.navigate(['/admin/init']);
  }
}
