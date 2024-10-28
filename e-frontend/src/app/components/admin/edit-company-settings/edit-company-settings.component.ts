import { CommonModule } from '@angular/common';
import { Component, Inject } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import {MatTabsModule} from '@angular/material/tabs';
import { countReset } from 'console';

@Component({
  selector: 'app-edit-company-settings',
  standalone: true,
  imports: [ReactiveFormsModule, MatTabsModule, CommonModule],
  templateUrl: './edit-company-settings.component.html',
  styleUrl: './edit-company-settings.component.scss'
})
export class EditCompanySettingsComponent {

  editForm: FormGroup;
  logoPreview: string | ArrayBuffer | null = null;
  logo: File | null = null;
  fileName = '';

  constructor(
    private fb: FormBuilder,
    public dialogRef: MatDialogRef<EditCompanySettingsComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any
  ) {
    // Inicializamos el formulario con los datos del negocio
    this.editForm = this.fb.group({
      name: [data.name, [Validators.required, Validators.maxLength(30)]],
      description: [data.description, Validators.required],
      courtRental: [data.courtRental, Validators.required],
    });
  }

  onLogoSelected(event: any): void {
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

  onSubmit(): void {
    if (this.editForm.valid) {
      const body = this.editForm.value;
      body.logo = this.logo
      this.dialogRef.close(body); 
    }
  }

  onCancel(): void {
    this.dialogRef.close();
  }
}
