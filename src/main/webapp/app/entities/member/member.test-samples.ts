import dayjs from 'dayjs/esm';

import { IMember, NewMember } from './member.model';

export const sampleWithRequiredData: IMember = {
  id: 13155,
  firstName: 'Sixtine',
  lastName: 'Huet',
  email: 'Francine.Garcia69@hotmail.fr',
  country: 'Birmanie',
  city: 'Villeurbanne',
  address: 'autour de quitte à',
  zipCode: '74842',
  birthDate: dayjs('2024-11-25'),
  signupDate: dayjs('2024-11-26'),
  rank: 'CHILDREN',
};

export const sampleWithPartialData: IMember = {
  id: 28007,
  memberUID: 'a7e31a9c-5b6b-4b0b-bc29-3d5b4c026c3b',
  firstName: 'Roland',
  lastName: 'Lucas',
  email: 'Aimee_Mathieu@hotmail.fr',
  country: 'République centrafricaine',
  city: 'Saint-Quentin',
  address: 'tard puisque',
  zipCode: '04269',
  birthDate: dayjs('2024-11-26'),
  signupDate: dayjs('2024-11-26'),
  rank: 'SPOUSE',
};

export const sampleWithFullData: IMember = {
  id: 10245,
  memberUID: '34434b96-9643-4260-812b-945ff2acd312',
  firstName: 'André',
  lastName: 'Lacroix',
  middleName: 'puisque outre',
  email: 'Georgette.Julien62@yahoo.fr',
  country: 'Croatie',
  city: 'Amiens',
  address: 'chef pendant que autrefois',
  zipCode: '12356',
  birthDate: dayjs('2024-11-25'),
  signupDate: dayjs('2024-11-26'),
  rank: 'PRIMARY_GUARDIAN',
};

export const sampleWithNewData: NewMember = {
  firstName: 'Athalie',
  lastName: 'Olivier',
  email: 'Gisele.Muller@hotmail.fr',
  country: 'Tunisie',
  city: 'Colombes',
  address: 'équipe tard',
  zipCode: '43955',
  birthDate: dayjs('2024-11-26'),
  signupDate: dayjs('2024-11-25'),
  rank: 'SPOUSE',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
