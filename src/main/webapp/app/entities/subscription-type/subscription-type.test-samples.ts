import { ISubscriptionType, NewSubscriptionType } from './subscription-type.model';

export const sampleWithRequiredData: ISubscriptionType = {
  id: 20686,
  label: 'tant remarquer',
};

export const sampleWithPartialData: ISubscriptionType = {
  id: 31560,
  label: 'passablement trop oh',
  summary: 'chef de cuisine gestionnaire',
};

export const sampleWithFullData: ISubscriptionType = {
  id: 11686,
  label: 'lorsque ronron lors',
  summary: 'à la merci',
};

export const sampleWithNewData: NewSubscriptionType = {
  label: 'novice drelin sympathique',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
