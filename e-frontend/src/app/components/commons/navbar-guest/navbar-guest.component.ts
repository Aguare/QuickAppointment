import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-navbar-guest',
  standalone: true,
  imports: [RouterModule, CommonModule],
  templateUrl: './navbar-guest.component.html',
  styleUrl: './navbar-guest.component.scss'
})
export class NavbarGuestComponent {

  navbarActive: boolean = false;

  toggleNavbar() {
		this.navbarActive = !this.navbarActive;
	}


}
