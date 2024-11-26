import dayjs from 'dayjs/esm';
import { IPayment } from 'app/entities/payment/payment.model';

export interface IMemberSubscription {
  id: number;
  subscriptionDate?: dayjs.Dayjs | null;
  payment?: Pick<IPayment, 'id'> | null;
}

export type NewMemberSubscription = Omit<IMemberSubscription, 'id'> & { id: null };
