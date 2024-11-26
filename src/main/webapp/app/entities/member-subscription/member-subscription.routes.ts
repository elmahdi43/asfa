import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import MemberSubscriptionResolve from './route/member-subscription-routing-resolve.service';

const memberSubscriptionRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/member-subscription.component').then(m => m.MemberSubscriptionComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/member-subscription-detail.component').then(m => m.MemberSubscriptionDetailComponent),
    resolve: {
      memberSubscription: MemberSubscriptionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/member-subscription-update.component').then(m => m.MemberSubscriptionUpdateComponent),
    resolve: {
      memberSubscription: MemberSubscriptionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/member-subscription-update.component').then(m => m.MemberSubscriptionUpdateComponent),
    resolve: {
      memberSubscription: MemberSubscriptionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default memberSubscriptionRoute;
