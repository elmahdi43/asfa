import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { IMemberSubscription } from '../member-subscription.model';

@Component({
  standalone: true,
  selector: 'jhi-member-subscription-detail',
  templateUrl: './member-subscription-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class MemberSubscriptionDetailComponent {
  memberSubscription = input<IMemberSubscription | null>(null);

  previousState(): void {
    window.history.back();
  }
}
