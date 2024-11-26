import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../member-subscription.test-samples';

import { MemberSubscriptionFormService } from './member-subscription-form.service';

describe('MemberSubscription Form Service', () => {
  let service: MemberSubscriptionFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(MemberSubscriptionFormService);
  });

  describe('Service methods', () => {
    describe('createMemberSubscriptionFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createMemberSubscriptionFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            subscriptionDate: expect.any(Object),
            payment: expect.any(Object),
          }),
        );
      });

      it('passing IMemberSubscription should create a new form with FormGroup', () => {
        const formGroup = service.createMemberSubscriptionFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            subscriptionDate: expect.any(Object),
            payment: expect.any(Object),
          }),
        );
      });
    });

    describe('getMemberSubscription', () => {
      it('should return NewMemberSubscription for default MemberSubscription initial value', () => {
        const formGroup = service.createMemberSubscriptionFormGroup(sampleWithNewData);

        const memberSubscription = service.getMemberSubscription(formGroup) as any;

        expect(memberSubscription).toMatchObject(sampleWithNewData);
      });

      it('should return NewMemberSubscription for empty MemberSubscription initial value', () => {
        const formGroup = service.createMemberSubscriptionFormGroup();

        const memberSubscription = service.getMemberSubscription(formGroup) as any;

        expect(memberSubscription).toMatchObject({});
      });

      it('should return IMemberSubscription', () => {
        const formGroup = service.createMemberSubscriptionFormGroup(sampleWithRequiredData);

        const memberSubscription = service.getMemberSubscription(formGroup) as any;

        expect(memberSubscription).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IMemberSubscription should not enable id FormControl', () => {
        const formGroup = service.createMemberSubscriptionFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewMemberSubscription should disable id FormControl', () => {
        const formGroup = service.createMemberSubscriptionFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
