export interface Page {
  id: number;
  pageName: string;
  direction: string;
  isAvailable: number;
  moduleName: string;
}

export interface MenuItem {
  module: string;
  pages: PageItem[];
}

export interface PageItem {
  pageName: string;
  direction: string;
  isAvailable: number;
}

export interface Company {
  id: number,
  name: string,
  description: string,
  logo: string
}
