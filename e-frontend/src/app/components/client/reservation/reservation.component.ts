import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { UUID } from 'crypto';

@Component({
  selector: 'app-reservation',
  standalone: true,
  imports: [],
  templateUrl: './reservation.component.html',
  styleUrl: './reservation.component.scss',
})
export class ReservationComponent implements OnInit {
  idService: number | undefined;

  constructor(private route: ActivatedRoute) {}

  ngOnInit(): void {
    this.route.queryParams.subscribe((params) => {
      this.idService = params['id'] ? +params['id'] : undefined;
    });
  }
}
