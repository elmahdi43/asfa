import dayjs from 'dayjs/esm';

import { IPayment, NewPayment } from './payment.model';

export const sampleWithRequiredData: IPayment = {
  id: 18409,
  paymentDate: dayjs('2024-11-26'),
  amount: 30618.9,
  timeStamp: dayjs('2024-11-25T17:01'),
};

export const sampleWithPartialData: IPayment = {
  id: 4818,
  paymentUID: 'c7f59a60-c934-4602-950a-663712a47037',
  paymentDate: dayjs('2024-11-26'),
  amount: 11063.04,
  timeStamp: dayjs('2024-11-26T03:20'),
};

export const sampleWithFullData: IPayment = {
  id: 11868,
  paymentUID: '1ea138c2-2674-4673-9711-01ba1bc51ac0',
  paymentDate: dayjs('2024-11-26'),
  amount: 3352.63,
  timeStamp: dayjs('2024-11-26T03:54'),
};

export const sampleWithNewData: NewPayment = {
  paymentDate: dayjs('2024-11-26'),
  amount: 17646.8,
  timeStamp: dayjs('2024-11-26T02:14'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
