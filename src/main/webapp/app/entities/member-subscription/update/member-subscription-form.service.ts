import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IMemberSubscription, NewMemberSubscription } from '../member-subscription.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IMemberSubscription for edit and NewMemberSubscriptionFormGroupInput for create.
 */
type MemberSubscriptionFormGroupInput = IMemberSubscription | PartialWithRequiredKeyOf<NewMemberSubscription>;

type MemberSubscriptionFormDefaults = Pick<NewMemberSubscription, 'id'>;

type MemberSubscriptionFormGroupContent = {
  id: FormControl<IMemberSubscription['id'] | NewMemberSubscription['id']>;
  subscriptionDate: FormControl<IMemberSubscription['subscriptionDate']>;
  payment: FormControl<IMemberSubscription['payment']>;
};

export type MemberSubscriptionFormGroup = FormGroup<MemberSubscriptionFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class MemberSubscriptionFormService {
  createMemberSubscriptionFormGroup(memberSubscription: MemberSubscriptionFormGroupInput = { id: null }): MemberSubscriptionFormGroup {
    const memberSubscriptionRawValue = {
      ...this.getFormDefaults(),
      ...memberSubscription,
    };
    return new FormGroup<MemberSubscriptionFormGroupContent>({
      id: new FormControl(
        { value: memberSubscriptionRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      subscriptionDate: new FormControl(memberSubscriptionRawValue.subscriptionDate, {
        validators: [Validators.required],
      }),
      payment: new FormControl(memberSubscriptionRawValue.payment),
    });
  }

  getMemberSubscription(form: MemberSubscriptionFormGroup): IMemberSubscription | NewMemberSubscription {
    return form.getRawValue() as IMemberSubscription | NewMemberSubscription;
  }

  resetForm(form: MemberSubscriptionFormGroup, memberSubscription: MemberSubscriptionFormGroupInput): void {
    const memberSubscriptionRawValue = { ...this.getFormDefaults(), ...memberSubscription };
    form.reset(
      {
        ...memberSubscriptionRawValue,
        id: { value: memberSubscriptionRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): MemberSubscriptionFormDefaults {
    return {
      id: null,
    };
  }
}
