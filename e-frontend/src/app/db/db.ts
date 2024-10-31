import { Company, Service, Place, Employee } from '../interfaces/interfaces';





// Data for the employees table
export const employees: Employee[] = [
  {
    id: 1,
    first_name: 'John',
    last_name: 'Doe',
    dpi: '1234567890101',
    date_birth: new Date('1990-01-01'),
    fkCompany: 15,
  },
  {
    id: 2,
    first_name: 'Jane',
    last_name: 'Smith',
    dpi: '9876543210102',
    date_birth: new Date('1985-05-15'),
    fkCompany: 15,
  },
  {
    id: 3,
    first_name: 'Paul',
    last_name: 'Brown',
    dpi: '1122334455667',
    date_birth: new Date('1992-09-23'),
    fkCompany: 15,
  },
];

// Data for the places table
export const places: Place[] = [
  { id: 1, name: 'Barber Shop', place: 'Downtown', fkCompany: 20 },
  { id: 2, name: 'Salon', place: 'Uptown', fkCompany: 20 },
  { id: 3, name: 'Spa', place: 'Suburbs', fkCompany: 20 },
];
