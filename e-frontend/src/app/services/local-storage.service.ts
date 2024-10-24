import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root',
})
export class LocalStorageService {

  USER_ID = 'id_user';
  USER_NAME = 'name_user';
  USER_ROLE = 'role_user'

  constructor() {}

  private isLocalStorageAvailable(): boolean {
    return typeof window !== 'undefined' && !!window.localStorage;
  }

  addSelectedTabIndex(index: number) {
    localStorage.setItem('selectedTabIndex', index.toString());
  }

  getSelectedTabIndex() {
    if (this.isLocalStorageAvailable()) {
      const savedTabIndex = localStorage.getItem('selectedTabIndex');
      return savedTabIndex;
    }
    return null;
  }

  setItem(key: string, value: any): void {
    if (this.isLocalStorageAvailable()) {
      localStorage.setItem(key, JSON.stringify(value));
    }
  }

  getItem(key: string): any {
    if (this.isLocalStorageAvailable()) {
      const item = localStorage.getItem(key);
      return item ? JSON.parse(item) : null;
    }
    return null;
  }
 
  deleteItem(key: string): any {
    if (this.isLocalStorageAvailable()) {
      localStorage.removeItem(key)
    }
  }

  setUserId(id: number): void {
    this.setItem(this.USER_ID, id);
  }

  setUserRole(id: number): void {
    this.setItem(this.USER_ROLE, id);
  }

  setUserName(name: string): void {
    this.setItem(this.USER_NAME, name);
  }
  

  getUserId(): number {
    return this.getItem(this.USER_ID);
  }
  
  getUserRole(): number {
    return this.getItem(this.USER_ROLE);
  }

  getUserName(): string {
    return this.getItem(this.USER_NAME);
  }

  clear(): void {
    if (this.isLocalStorageAvailable()) {
      localStorage.clear();
    }
  }

  logout(): void {
    this.clear();
  }
}
