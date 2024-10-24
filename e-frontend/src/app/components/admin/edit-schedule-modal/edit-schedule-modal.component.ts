import { Component, Inject } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatSelectModule } from '@angular/material/select';
import {MatDialogModule} from '@angular/material/dialog';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-edit-schedule-modal',
  standalone: true,
  imports: [MatFormFieldModule, MatSelectModule, MatDialogModule, ReactiveFormsModule, CommonModule],
  templateUrl: './edit-schedule-modal.component.html',
  styleUrl: './edit-schedule-modal.component.scss'
})
export class EditScheduleModalComponent {
  editForm: FormGroup;

  constructor(
    private fb: FormBuilder,
    public dialogRef: MatDialogRef<EditScheduleModalComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any
  ) {
    this.editForm = this.fb.group({
      openingTime: [data.openingTime, Validators.required],
      closingTime: [data.closingTime, Validators.required]
    });
  }

  save() {
    if (this.editForm.valid) {
      this.dialogRef.close({
        fkCompany: this.data.fkCompany,
        fkDay: this.data.fkDay,
        openingTime: this.editForm.get('openingTime')?.value,
        closingTime: this.editForm.get('closingTime')?.value
      });
    }
  }

  close() {
    this.dialogRef.close();
  }
}
