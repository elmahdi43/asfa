import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IPayment } from 'app/entities/payment/payment.model';
import { PaymentService } from 'app/entities/payment/service/payment.service';
import { MemberSubscriptionService } from '../service/member-subscription.service';
import { IMemberSubscription } from '../member-subscription.model';
import { MemberSubscriptionFormService } from './member-subscription-form.service';

import { MemberSubscriptionUpdateComponent } from './member-subscription-update.component';

describe('MemberSubscription Management Update Component', () => {
  let comp: MemberSubscriptionUpdateComponent;
  let fixture: ComponentFixture<MemberSubscriptionUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let memberSubscriptionFormService: MemberSubscriptionFormService;
  let memberSubscriptionService: MemberSubscriptionService;
  let paymentService: PaymentService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [MemberSubscriptionUpdateComponent],
      providers: [
        provideHttpClient(),
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(MemberSubscriptionUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(MemberSubscriptionUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    memberSubscriptionFormService = TestBed.inject(MemberSubscriptionFormService);
    memberSubscriptionService = TestBed.inject(MemberSubscriptionService);
    paymentService = TestBed.inject(PaymentService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Payment query and add missing value', () => {
      const memberSubscription: IMemberSubscription = { id: 456 };
      const payment: IPayment = { id: 12173 };
      memberSubscription.payment = payment;

      const paymentCollection: IPayment[] = [{ id: 6035 }];
      jest.spyOn(paymentService, 'query').mockReturnValue(of(new HttpResponse({ body: paymentCollection })));
      const additionalPayments = [payment];
      const expectedCollection: IPayment[] = [...additionalPayments, ...paymentCollection];
      jest.spyOn(paymentService, 'addPaymentToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ memberSubscription });
      comp.ngOnInit();

      expect(paymentService.query).toHaveBeenCalled();
      expect(paymentService.addPaymentToCollectionIfMissing).toHaveBeenCalledWith(
        paymentCollection,
        ...additionalPayments.map(expect.objectContaining),
      );
      expect(comp.paymentsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const memberSubscription: IMemberSubscription = { id: 456 };
      const payment: IPayment = { id: 29236 };
      memberSubscription.payment = payment;

      activatedRoute.data = of({ memberSubscription });
      comp.ngOnInit();

      expect(comp.paymentsSharedCollection).toContain(payment);
      expect(comp.memberSubscription).toEqual(memberSubscription);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMemberSubscription>>();
      const memberSubscription = { id: 123 };
      jest.spyOn(memberSubscriptionFormService, 'getMemberSubscription').mockReturnValue(memberSubscription);
      jest.spyOn(memberSubscriptionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ memberSubscription });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: memberSubscription }));
      saveSubject.complete();

      // THEN
      expect(memberSubscriptionFormService.getMemberSubscription).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(memberSubscriptionService.update).toHaveBeenCalledWith(expect.objectContaining(memberSubscription));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMemberSubscription>>();
      const memberSubscription = { id: 123 };
      jest.spyOn(memberSubscriptionFormService, 'getMemberSubscription').mockReturnValue({ id: null });
      jest.spyOn(memberSubscriptionService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ memberSubscription: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: memberSubscription }));
      saveSubject.complete();

      // THEN
      expect(memberSubscriptionFormService.getMemberSubscription).toHaveBeenCalled();
      expect(memberSubscriptionService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMemberSubscription>>();
      const memberSubscription = { id: 123 };
      jest.spyOn(memberSubscriptionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ memberSubscription });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(memberSubscriptionService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('comparePayment', () => {
      it('Should forward to paymentService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(paymentService, 'comparePayment');
        comp.comparePayment(entity, entity2);
        expect(paymentService.comparePayment).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
