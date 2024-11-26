import { IMemberSubscription } from 'app/entities/member-subscription/member-subscription.model';

export interface ISubscriptionType {
  id: number;
  label?: string | null;
  summary?: string | null;
  subscription?: Pick<IMemberSubscription, 'id'> | null;
}

export type NewSubscriptionType = Omit<ISubscriptionType, 'id'> & { id: null };
