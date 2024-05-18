import { IUser } from './user.model';

export const sampleWithRequiredData: IUser = {
  id: 31041,
  login: '7XOF@_',
};

export const sampleWithPartialData: IUser = {
  id: 29114,
  login: 'MRfl',
};

export const sampleWithFullData: IUser = {
  id: 9385,
  login: 'fODxv',
};
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
