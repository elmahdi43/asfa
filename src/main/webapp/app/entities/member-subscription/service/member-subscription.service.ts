import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, map } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IMemberSubscription, NewMemberSubscription } from '../member-subscription.model';

export type PartialUpdateMemberSubscription = Partial<IMemberSubscription> & Pick<IMemberSubscription, 'id'>;

type RestOf<T extends IMemberSubscription | NewMemberSubscription> = Omit<T, 'subscriptionDate'> & {
  subscriptionDate?: string | null;
};

export type RestMemberSubscription = RestOf<IMemberSubscription>;

export type NewRestMemberSubscription = RestOf<NewMemberSubscription>;

export type PartialUpdateRestMemberSubscription = RestOf<PartialUpdateMemberSubscription>;

export type EntityResponseType = HttpResponse<IMemberSubscription>;
export type EntityArrayResponseType = HttpResponse<IMemberSubscription[]>;

@Injectable({ providedIn: 'root' })
export class MemberSubscriptionService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/member-subscriptions');

  create(memberSubscription: NewMemberSubscription): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(memberSubscription);
    return this.http
      .post<RestMemberSubscription>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(memberSubscription: IMemberSubscription): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(memberSubscription);
    return this.http
      .put<RestMemberSubscription>(`${this.resourceUrl}/${this.getMemberSubscriptionIdentifier(memberSubscription)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(memberSubscription: PartialUpdateMemberSubscription): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(memberSubscription);
    return this.http
      .patch<RestMemberSubscription>(`${this.resourceUrl}/${this.getMemberSubscriptionIdentifier(memberSubscription)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestMemberSubscription>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestMemberSubscription[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getMemberSubscriptionIdentifier(memberSubscription: Pick<IMemberSubscription, 'id'>): number {
    return memberSubscription.id;
  }

  compareMemberSubscription(o1: Pick<IMemberSubscription, 'id'> | null, o2: Pick<IMemberSubscription, 'id'> | null): boolean {
    return o1 && o2 ? this.getMemberSubscriptionIdentifier(o1) === this.getMemberSubscriptionIdentifier(o2) : o1 === o2;
  }

  addMemberSubscriptionToCollectionIfMissing<Type extends Pick<IMemberSubscription, 'id'>>(
    memberSubscriptionCollection: Type[],
    ...memberSubscriptionsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const memberSubscriptions: Type[] = memberSubscriptionsToCheck.filter(isPresent);
    if (memberSubscriptions.length > 0) {
      const memberSubscriptionCollectionIdentifiers = memberSubscriptionCollection.map(memberSubscriptionItem =>
        this.getMemberSubscriptionIdentifier(memberSubscriptionItem),
      );
      const memberSubscriptionsToAdd = memberSubscriptions.filter(memberSubscriptionItem => {
        const memberSubscriptionIdentifier = this.getMemberSubscriptionIdentifier(memberSubscriptionItem);
        if (memberSubscriptionCollectionIdentifiers.includes(memberSubscriptionIdentifier)) {
          return false;
        }
        memberSubscriptionCollectionIdentifiers.push(memberSubscriptionIdentifier);
        return true;
      });
      return [...memberSubscriptionsToAdd, ...memberSubscriptionCollection];
    }
    return memberSubscriptionCollection;
  }

  protected convertDateFromClient<T extends IMemberSubscription | NewMemberSubscription | PartialUpdateMemberSubscription>(
    memberSubscription: T,
  ): RestOf<T> {
    return {
      ...memberSubscription,
      subscriptionDate: memberSubscription.subscriptionDate?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restMemberSubscription: RestMemberSubscription): IMemberSubscription {
    return {
      ...restMemberSubscription,
      subscriptionDate: restMemberSubscription.subscriptionDate ? dayjs(restMemberSubscription.subscriptionDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestMemberSubscription>): HttpResponse<IMemberSubscription> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestMemberSubscription[]>): HttpResponse<IMemberSubscription[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
