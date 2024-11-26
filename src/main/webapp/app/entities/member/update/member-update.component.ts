import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IMemberSubscription } from 'app/entities/member-subscription/member-subscription.model';
import { MemberSubscriptionService } from 'app/entities/member-subscription/service/member-subscription.service';
import { FamilyRank } from 'app/entities/enumerations/family-rank.model';
import { MemberService } from '../service/member.service';
import { IMember } from '../member.model';
import { MemberFormGroup, MemberFormService } from './member-form.service';

@Component({
  standalone: true,
  selector: 'jhi-member-update',
  templateUrl: './member-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class MemberUpdateComponent implements OnInit {
  isSaving = false;
  member: IMember | null = null;
  familyRankValues = Object.keys(FamilyRank);

  membersSharedCollection: IMember[] = [];
  memberSubscriptionsSharedCollection: IMemberSubscription[] = [];

  protected memberService = inject(MemberService);
  protected memberFormService = inject(MemberFormService);
  protected memberSubscriptionService = inject(MemberSubscriptionService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: MemberFormGroup = this.memberFormService.createMemberFormGroup();

  compareMember = (o1: IMember | null, o2: IMember | null): boolean => this.memberService.compareMember(o1, o2);

  compareMemberSubscription = (o1: IMemberSubscription | null, o2: IMemberSubscription | null): boolean =>
    this.memberSubscriptionService.compareMemberSubscription(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ member }) => {
      this.member = member;
      if (member) {
        this.updateForm(member);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const member = this.memberFormService.getMember(this.editForm);
    if (member.id !== null) {
      this.subscribeToSaveResponse(this.memberService.update(member));
    } else {
      this.subscribeToSaveResponse(this.memberService.create(member));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMember>>): void {
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

  protected updateForm(member: IMember): void {
    this.member = member;
    this.memberFormService.resetForm(this.editForm, member);

    this.membersSharedCollection = this.memberService.addMemberToCollectionIfMissing<IMember>(this.membersSharedCollection, member.member);
    this.memberSubscriptionsSharedCollection =
      this.memberSubscriptionService.addMemberSubscriptionToCollectionIfMissing<IMemberSubscription>(
        this.memberSubscriptionsSharedCollection,
        member.subscription,
      );
  }

  protected loadRelationshipsOptions(): void {
    this.memberService
      .query()
      .pipe(map((res: HttpResponse<IMember[]>) => res.body ?? []))
      .pipe(map((members: IMember[]) => this.memberService.addMemberToCollectionIfMissing<IMember>(members, this.member?.member)))
      .subscribe((members: IMember[]) => (this.membersSharedCollection = members));

    this.memberSubscriptionService
      .query()
      .pipe(map((res: HttpResponse<IMemberSubscription[]>) => res.body ?? []))
      .pipe(
        map((memberSubscriptions: IMemberSubscription[]) =>
          this.memberSubscriptionService.addMemberSubscriptionToCollectionIfMissing<IMemberSubscription>(
            memberSubscriptions,
            this.member?.subscription,
          ),
        ),
      )
      .subscribe((memberSubscriptions: IMemberSubscription[]) => (this.memberSubscriptionsSharedCollection = memberSubscriptions));
  }
}
