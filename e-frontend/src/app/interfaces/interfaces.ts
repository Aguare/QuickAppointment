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

export interface Service {
  id: number,
  name: string,
  description: string,
  duration: number // In Minutes
  fk_company: number
}

export interface Employee {
  id: number,
  first_name: string,
  last_name: string,
  dpi: string,
  date_birth: Date,
}

export interface Place {
  id: number,
  name: string,
  place: string
}
