import { IPaymentMethod, NewPaymentMethod } from './payment-method.model';

export const sampleWithRequiredData: IPaymentMethod = {
  id: 27810,
  label: 'avant atchoum',
};

export const sampleWithPartialData: IPaymentMethod = {
  id: 16447,
  label: 'alentour impromptu',
};

export const sampleWithFullData: IPaymentMethod = {
  id: 25509,
  label: 'à bas de hirsute préoccuper',
};

export const sampleWithNewData: NewPaymentMethod = {
  label: 'épanouir',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
