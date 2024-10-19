import {
  Component,
  CUSTOM_ELEMENTS_SCHEMA,
  inject,
  OnInit,
  TemplateRef,
} from '@angular/core';
import { NavbarGuestComponent } from "../navbar-guest/navbar-guest.component";

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [NavbarGuestComponent],
  templateUrl: './home.component.html',
  styleUrl: './home.component.scss',
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
})
export class HomeComponent implements OnInit {
  constructor() {}

  ngOnInit(): void {}
}
