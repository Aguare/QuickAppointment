import { Component, Inject, OnInit, PLATFORM_ID } from '@angular/core';
import { NavbarComponent } from '../../commons/navbar/navbar.component';
import { CommonModule, isPlatformBrowser } from '@angular/common';
import { ChartData, ChartType } from 'chart.js';
import { BaseChartDirective } from 'ng2-charts';
import { ClientService } from '../../../services/client.service';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [NavbarComponent, CommonModule, BaseChartDirective],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.scss',
})
export class DashboardComponent implements OnInit {
  isBrowser?: boolean;

  barChartOptions = {
    responsive: true,
  };
  barChartLabels: any = [];
  barChartData: any = [];

  barChartOptions1 = {
    responsive: true,
  };
  barChartLabels1: any = [];
  barChartData1: any = [];

  constructor(
    @Inject(PLATFORM_ID) private platformId: any
  ) {}

  ngOnInit(): void {
    this.isBrowser = isPlatformBrowser(this.platformId);

    
  }

  dataArray(array: any) {
    let namesArray : any= [];
    let dataArray :any= [];

    array.forEach((item:any) => {
        namesArray.push(item.year);
        dataArray.push(item.appointments);
    });

    return { namesArray, dataArray };
}
}
