import { Component, OnInit } from '@angular/core';
import jsPDF from 'jspdf';
import html2canvas from 'html2canvas';
import { CommonModule, DatePipe } from '@angular/common';
import { MatButtonModule } from '@angular/material/button';
import { LocalStorageService } from '../../../services/local-storage.service';
import { AdminService } from '../../../services/admin.service';
import { Company } from '../../../interfaces/interfaces';
import { ImagePipe } from '../../../pipes/image.pipe';
import { employees } from '../../../db/db';
import { NavbarComponent } from "../../commons/navbar/navbar.component";

@Component({
  selector: 'app-bill-view',
  standalone: true,
  imports: [MatButtonModule, CommonModule, ImagePipe, NavbarComponent],
  templateUrl: './bill-view.component.html',
  styleUrl: './bill-view.component.scss'
})
export class BillViewComponent implements OnInit{

  companyLogo: string = '';
  companyName: string = '';
  data: any = {
    date: '',
    hour: '',
    employee: '',
    type: '',
    cui: '',
    direction: '',
    nit: '',
    place: ''
  };

  constructor(private localStorageService: LocalStorageService, private adminService: AdminService){}

  ngOnInit(): void {
    
    const data = this.localStorageService.getItem('bill');
    this.data = data;
    if(data){
      this.adminService.getCompanyById(data.idCompany).subscribe({
        next:(value: Company) =>{
          this.companyLogo = value.logo;
          this.companyName = value.name;
        }, error:(err) =>{
          console.log(err);
          
        },
      })
    }

  }

  downloadPDF(): void {
    const invoiceElement = document.getElementById('invoice');
    if (invoiceElement){
      html2canvas(invoiceElement, {useCORS: true}).then((canvas) => {
        const imgData = canvas.toDataURL('image/png');
        const pdf = new jsPDF('p', 'mm', 'a4');
        const imgWidth = 210; 
        const imgHeight = (canvas.height * imgWidth) / canvas.width;
        pdf.addImage(imgData, 'PNG', 0, 0, imgWidth, imgHeight);
        pdf.save('factura.pdf');
      });
    }
  }
}
