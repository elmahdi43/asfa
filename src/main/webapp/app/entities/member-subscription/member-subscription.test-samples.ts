import dayjs from 'dayjs/esm';

import { IMemberSubscription, NewMemberSubscription } from './member-subscription.model';

export const sampleWithRequiredData: IMemberSubscription = {
  id: 9361,
  subscriptionDate: dayjs('2024-11-26'),
};

export const sampleWithPartialData: IMemberSubscription = {
  id: 15108,
  subscriptionDate: dayjs('2024-11-26'),
};

export const sampleWithFullData: IMemberSubscription = {
  id: 5604,
  subscriptionDate: dayjs('2024-11-26'),
};

export const sampleWithNewData: NewMemberSubscription = {
  subscriptionDate: dayjs('2024-11-25'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
