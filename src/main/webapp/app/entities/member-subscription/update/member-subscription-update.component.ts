import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IPayment } from 'app/entities/payment/payment.model';
import { PaymentService } from 'app/entities/payment/service/payment.service';
import { IMemberSubscription } from '../member-subscription.model';
import { MemberSubscriptionService } from '../service/member-subscription.service';
import { MemberSubscriptionFormGroup, MemberSubscriptionFormService } from './member-subscription-form.service';

@Component({
  standalone: true,
  selector: 'jhi-member-subscription-update',
  templateUrl: './member-subscription-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class MemberSubscriptionUpdateComponent implements OnInit {
  isSaving = false;
  memberSubscription: IMemberSubscription | null = null;

  paymentsSharedCollection: IPayment[] = [];

  protected memberSubscriptionService = inject(MemberSubscriptionService);
  protected memberSubscriptionFormService = inject(MemberSubscriptionFormService);
  protected paymentService = inject(PaymentService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: MemberSubscriptionFormGroup = this.memberSubscriptionFormService.createMemberSubscriptionFormGroup();

  comparePayment = (o1: IPayment | null, o2: IPayment | null): boolean => this.paymentService.comparePayment(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ memberSubscription }) => {
      this.memberSubscription = memberSubscription;
      if (memberSubscription) {
        this.updateForm(memberSubscription);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const memberSubscription = this.memberSubscriptionFormService.getMemberSubscription(this.editForm);
    if (memberSubscription.id !== null) {
      this.subscribeToSaveResponse(this.memberSubscriptionService.update(memberSubscription));
    } else {
      this.subscribeToSaveResponse(this.memberSubscriptionService.create(memberSubscription));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMemberSubscription>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(memberSubscription: IMemberSubscription): void {
    this.memberSubscription = memberSubscription;
    this.memberSubscriptionFormService.resetForm(this.editForm, memberSubscription);

    this.paymentsSharedCollection = this.paymentService.addPaymentToCollectionIfMissing<IPayment>(
      this.paymentsSharedCollection,
      memberSubscription.payment,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.paymentService
      .query()
      .pipe(map((res: HttpResponse<IPayment[]>) => res.body ?? []))
      .pipe(
        map((payments: IPayment[]) =>
          this.paymentService.addPaymentToCollectionIfMissing<IPayment>(payments, this.memberSubscription?.payment),
        ),
      )
      .subscribe((payments: IPayment[]) => (this.paymentsSharedCollection = payments));
  }
}
