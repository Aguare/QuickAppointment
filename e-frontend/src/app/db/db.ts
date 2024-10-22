import { Company, Service, Place, Employee } from '../interfaces/interfaces';

export const companies: Company[] = [
  {
    id: 1,
    name: 'Elite Barber Shop',
    description:
      'Barbería especializada en cortes de cabello y afeitado. Ambiente relajado y moderno',
    logo: 'https://static.vecteezy.com/system/resources/previews/007/619/786/non_2x/vintage-barbershop-logo-design-template-vector.jpg', // Cambiar a una URL de imagen real
  },
  {
    id: 2,
    name: 'Modern Grooming',
    description:
      'Barbería de lujo con servicio completo de cuidado personal masculino.',
    logo: 'https://thumbs.dreamstime.com/b/logotipo-de-la-preparaci%C3%B3n-57460430.jpg', // Cambiar a una URL de imagen real
  },
  {
    id: 3,
    name: 'SaludTotal Clínica',
    description:
      'Clínica especializada en medicina general y servicios de urgencias.',
    logo: 'https://thumbs.dreamstime.com/b/logo-del-icono-de-concepto-salud-coraz%C3%B3n-forma-simple-rojo-con-la-l%C3%ADnea-blanca-dentro-169861715.jpg', // Cambiar a una URL de imagen real
  },
  {
    id: 4,
    name: 'Clínica Dental Plus',
    description:
      'Ofrecemos servicios dentales completos, desde limpiezas hasta tratamientos avanzados.',
    logo: 'https://img.freepik.com/vector-gratis/gradiente-logotipo-moderno-clinica-dental_62951-54.jpg', // Cambiar a una URL de imagen real
  },
  {
    id: 5,
    name: 'FutbolManía',
    description:
      'Alquiler de canchas deportivas para fútbol, basket y voleibol. Equipadas con iluminación nocturna.',
    logo: 'https://img.freepik.com/vector-premium/campo-futbol-vectores-logotipo-futbol-su-liga-club-equipo-o-torneo_172933-36.jpg', // Cambiar a una URL de imagen real
  },
];



// Data for the employees table
export const employees: Employee[] = [
  {
    id: 1,
    first_name: 'John',
    last_name: 'Doe',
    dpi: '1234567890101',
    date_birth: new Date('1990-01-01'),
    fk_company: 15,
  },
  {
    id: 2,
    first_name: 'Jane',
    last_name: 'Smith',
    dpi: '9876543210102',
    date_birth: new Date('1985-05-15'),
    fk_company: 15,
  },
  {
    id: 3,
    first_name: 'Paul',
    last_name: 'Brown',
    dpi: '1122334455667',
    date_birth: new Date('1992-09-23'),
    fk_company: 15,
  },
];

// Data for the places table
export const places: Place[] = [
  { id: 1, name: 'Barber Shop', place: 'Downtown', fk_company: 20 },
  { id: 2, name: 'Salon', place: 'Uptown', fk_company: 20 },
  { id: 3, name: 'Spa', place: 'Suburbs', fk_company: 20 },
];
