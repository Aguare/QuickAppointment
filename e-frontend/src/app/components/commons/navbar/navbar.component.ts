import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { CookieService } from 'ngx-cookie-service';
import { MenuItem, Page, PageItem } from '../../../interfaces/interfaces';

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './navbar.component.html',
  styleUrl: './navbar.component.scss',
})
export class NavbarComponent implements OnInit {
  pages: Page[] = [];
  pagesNavBar: MenuItem[] = [];
  userName: any;
  isActive: boolean = false;
  activeModule: string | null = null;

  constructor(private _router: Router, private _cookieService: CookieService) {}

  ngOnInit(): void {
    this.pages = [
      {
        id: 1,
        pageName: 'Home',
        direction: '/home',
        isAvailable: 1,
        moduleName: 'MainModule',
      },
      {
        id: 2,
        pageName: 'About',
        direction: '/about',
        isAvailable: 1,
        moduleName: 'MainModule',
      },
      {
        id: 3,
        pageName: 'Contact',
        direction: '/contact',
        isAvailable: 0,
        moduleName: 'MainModule',
      },

      {
        id: 4,
        pageName: 'Dashboard',
        direction: '/dashboard',
        isAvailable: 1,
        moduleName: 'AdminModule',
      },
      {
        id: 5,
        pageName: 'Settings',
        direction: '/settings',
        isAvailable: 1,
        moduleName: 'AdminModule',
      },
    ];

    this.pagesNavBar = this.groupPagesByModule(this.pages);
  }

  /**
   * Method to convert pages to navbarMenu
   * @param pages
   * @returns
   */
  groupPagesByModule(pages: Page[]): MenuItem[] {
    const groupedPages: { [key: string]: PageItem[] } = {};

    pages.forEach((page) => {
      if (!groupedPages[page.moduleName]) {
        groupedPages[page.moduleName] = [];
      }
      groupedPages[page.moduleName].push({
        pageName: page.pageName,
        direction: page.direction,
        isAvailable: page.isAvailable,
      });
    });

    return Object.keys(groupedPages).map((moduleName) => ({
      module: moduleName,
      pages: groupedPages[moduleName],
    }));
  }

  logout() {
    this._cookieService.delete('token');
    this._router.navigate(['/home']);
  }

  toggleNavbar() {
    this.isActive = !this.isActive;
  }

  toggleSubmenu(module: string) {
    this.activeModule = this.activeModule === module ? null : module;
  }

  myAccount() {
    this._router.navigate([`/account/${this.userName}`]);
  }
}
