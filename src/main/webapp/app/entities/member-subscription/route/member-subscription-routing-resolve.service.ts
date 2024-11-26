import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IMemberSubscription } from '../member-subscription.model';
import { MemberSubscriptionService } from '../service/member-subscription.service';

const memberSubscriptionResolve = (route: ActivatedRouteSnapshot): Observable<null | IMemberSubscription> => {
  const id = route.params.id;
  if (id) {
    return inject(MemberSubscriptionService)
      .find(id)
      .pipe(
        mergeMap((memberSubscription: HttpResponse<IMemberSubscription>) => {
          if (memberSubscription.body) {
            return of(memberSubscription.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default memberSubscriptionResolve;
