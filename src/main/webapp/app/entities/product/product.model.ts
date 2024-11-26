import dayjs from 'dayjs/esm';
import { IMemberSubscription } from 'app/entities/member-subscription/member-subscription.model';
import { ProductTypeEnum } from 'app/entities/enumerations/product-type-enum.model';

export interface IProduct {
  id: number;
  productUID?: string | null;
  contractNumber?: string | null;
  startDate?: dayjs.Dayjs | null;
  endDate?: dayjs.Dayjs | null;
  summary?: string | null;
  productType?: keyof typeof ProductTypeEnum | null;
  subscription?: Pick<IMemberSubscription, 'id'> | null;
}

export type NewProduct = Omit<IProduct, 'id'> & { id: null };
