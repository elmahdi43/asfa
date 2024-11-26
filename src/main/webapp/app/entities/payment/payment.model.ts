import dayjs from 'dayjs/esm';

export interface IPayment {
  id: number;
  paymentUID?: string | null;
  paymentDate?: dayjs.Dayjs | null;
  amount?: number | null;
  timeStamp?: dayjs.Dayjs | null;
}

export type NewPayment = Omit<IPayment, 'id'> & { id: null };
