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
  id: number;
  name: string;
  description: string;
  logo: string;
  courtRental: boolean;
}

export interface Service {
  id: number;
  name: string;
  description: string;
  duration: number; // In Minutes
  price: number; // In Minutes
  fkCompany: number;
}

export interface Employee {
  id: number;
  first_name: string;
  last_name: string;
  dpi: string;
  date_birth: Date;
  fkCompany: number;
}

export interface Place {
  id: number;
  name: string;
  place: string;
  fkCompany: number;
}

export interface Role {
  id: number;
  name: string;
  description: string;
  allowCreate?: boolean;
  allowEdit?: boolean;
  allowDelete?: boolean;
}

export interface User {
  id: number;
  email: string;
  username: string;
  isClient: boolean;
  password?: string;
  idRole: number;
  roleName: string;
}

export interface Schedule {
  fkCompany: number;
  fkDay: number;
  openingTime: string;
  closingTime: string;
}

export interface Appointment {
  id: number;
  fkUser: number;
  date: string;
  hour: string;
  fkEmployee: number;
  fkType: number;
  fkPlace: number;
  isConfirmated: boolean;
  isCanceled: boolean;
}

export interface MyAppointment {
  id: number;
  date: string;
  hour: string;
  service: string;
  price: number;
  first_name: string;
  last_name: string;
  place: string;
  fkCompany: number;
  is_confirmated: boolean;
  fkService: number;
}

export interface Billing {
  id: number;
  nit: string;
  cui: string;
  direction: string;
  fkUser: number;
  auth2FA: boolean;
}

export interface AppointmentReport {
  id: number;
  username: string;
  email: string;
  date: string;
  hour: string;
  service: string;
  price: number;
}

export interface UserDto {
  id: number;
  email: string;
  username: string;
  password: string;
  rol: string;
  idRole: number;
}
