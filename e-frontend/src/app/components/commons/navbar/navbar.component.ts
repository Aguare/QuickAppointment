import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { CookieService } from 'ngx-cookie-service';
import { MenuItem, Page, PageItem } from '../../../interfaces/interfaces';
import { LocalStorageService } from '../../../services/local-storage.service';
import { UserService } from '../../../services/user.service';

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

  constructor(
    private _router: Router,
    private locaStorageService: LocalStorageService,
    private userService: UserService
  ) {}

  ngOnInit(): void {
  
    const idUser = this.locaStorageService.getUserId();

    this.userService.getPages(idUser).subscribe({
      next: (value: Page[]) => {
        this.pages = value;
        this.pagesNavBar = this.groupPagesByModule(this.pages);
      },
      error: (err) => {
        console.log(err);
      },
    });

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
    this.locaStorageService.logout();
    this._router.navigate(['/']);
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
