import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IMemberSubscription } from 'app/entities/member-subscription/member-subscription.model';
import { MemberSubscriptionService } from 'app/entities/member-subscription/service/member-subscription.service';
import { ISubscriptionType } from '../subscription-type.model';
import { SubscriptionTypeService } from '../service/subscription-type.service';
import { SubscriptionTypeFormGroup, SubscriptionTypeFormService } from './subscription-type-form.service';

@Component({
  standalone: true,
  selector: 'jhi-subscription-type-update',
  templateUrl: './subscription-type-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class SubscriptionTypeUpdateComponent implements OnInit {
  isSaving = false;
  subscriptionType: ISubscriptionType | null = null;

  memberSubscriptionsSharedCollection: IMemberSubscription[] = [];

  protected subscriptionTypeService = inject(SubscriptionTypeService);
  protected subscriptionTypeFormService = inject(SubscriptionTypeFormService);
  protected memberSubscriptionService = inject(MemberSubscriptionService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: SubscriptionTypeFormGroup = this.subscriptionTypeFormService.createSubscriptionTypeFormGroup();

  compareMemberSubscription = (o1: IMemberSubscription | null, o2: IMemberSubscription | null): boolean =>
    this.memberSubscriptionService.compareMemberSubscription(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ subscriptionType }) => {
      this.subscriptionType = subscriptionType;
      if (subscriptionType) {
        this.updateForm(subscriptionType);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const subscriptionType = this.subscriptionTypeFormService.getSubscriptionType(this.editForm);
    if (subscriptionType.id !== null) {
      this.subscribeToSaveResponse(this.subscriptionTypeService.update(subscriptionType));
    } else {
      this.subscribeToSaveResponse(this.subscriptionTypeService.create(subscriptionType));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISubscriptionType>>): void {
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

  protected updateForm(subscriptionType: ISubscriptionType): void {
    this.subscriptionType = subscriptionType;
    this.subscriptionTypeFormService.resetForm(this.editForm, subscriptionType);

    this.memberSubscriptionsSharedCollection =
      this.memberSubscriptionService.addMemberSubscriptionToCollectionIfMissing<IMemberSubscription>(
        this.memberSubscriptionsSharedCollection,
        subscriptionType.subscription,
      );
  }

  protected loadRelationshipsOptions(): void {
    this.memberSubscriptionService
      .query()
      .pipe(map((res: HttpResponse<IMemberSubscription[]>) => res.body ?? []))
      .pipe(
        map((memberSubscriptions: IMemberSubscription[]) =>
          this.memberSubscriptionService.addMemberSubscriptionToCollectionIfMissing<IMemberSubscription>(
            memberSubscriptions,
            this.subscriptionType?.subscription,
          ),
        ),
      )
      .subscribe((memberSubscriptions: IMemberSubscription[]) => (this.memberSubscriptionsSharedCollection = memberSubscriptions));
  }
}
