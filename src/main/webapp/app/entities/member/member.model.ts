import dayjs from 'dayjs/esm';
import { IMemberSubscription } from 'app/entities/member-subscription/member-subscription.model';
import { FamilyRank } from 'app/entities/enumerations/family-rank.model';

export interface IMember {
  id: number;
  memberUID?: string | null;
  firstName?: string | null;
  lastName?: string | null;
  middleName?: string | null;
  email?: string | null;
  country?: string | null;
  city?: string | null;
  address?: string | null;
  zipCode?: string | null;
  birthDate?: dayjs.Dayjs | null;
  signupDate?: dayjs.Dayjs | null;
  rank?: keyof typeof FamilyRank | null;
  subscription?: Pick<IMemberSubscription, 'id'> | null;
  member?: Pick<IMember, 'id'> | null;
}

export type NewMember = Omit<IMember, 'id'> & { id: null };
