import { IAuthority, NewAuthority } from './authority.model';

export const sampleWithRequiredData: IAuthority = {
  name: '7dfa5887-0d71-4dfa-8201-986405314f0d',
};

export const sampleWithPartialData: IAuthority = {
  name: 'ca365411-dacd-4b54-b841-223d06137c11',
};

export const sampleWithFullData: IAuthority = {
  name: '21584714-4482-4b05-9ec6-ba1efdc03177',
};

export const sampleWithNewData: NewAuthority = {
  name: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
