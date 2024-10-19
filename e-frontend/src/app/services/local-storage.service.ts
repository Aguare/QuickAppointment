import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root',
})
export class LocalStorageService {
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
}
