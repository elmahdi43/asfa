import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IMemberSubscription } from 'app/entities/member-subscription/member-subscription.model';
import { MemberSubscriptionService } from 'app/entities/member-subscription/service/member-subscription.service';
import { ProductTypeEnum } from 'app/entities/enumerations/product-type-enum.model';
import { ProductService } from '../service/product.service';
import { IProduct } from '../product.model';
import { ProductFormGroup, ProductFormService } from './product-form.service';

@Component({
  standalone: true,
  selector: 'jhi-product-update',
  templateUrl: './product-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class ProductUpdateComponent implements OnInit {
  isSaving = false;
  product: IProduct | null = null;
  productTypeEnumValues = Object.keys(ProductTypeEnum);

  memberSubscriptionsSharedCollection: IMemberSubscription[] = [];

  protected productService = inject(ProductService);
  protected productFormService = inject(ProductFormService);
  protected memberSubscriptionService = inject(MemberSubscriptionService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ProductFormGroup = this.productFormService.createProductFormGroup();

  compareMemberSubscription = (o1: IMemberSubscription | null, o2: IMemberSubscription | null): boolean =>
    this.memberSubscriptionService.compareMemberSubscription(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ product }) => {
      this.product = product;
      if (product) {
        this.updateForm(product);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const product = this.productFormService.getProduct(this.editForm);
    if (product.id !== null) {
      this.subscribeToSaveResponse(this.productService.update(product));
    } else {
      this.subscribeToSaveResponse(this.productService.create(product));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IProduct>>): void {
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

  protected updateForm(product: IProduct): void {
    this.product = product;
    this.productFormService.resetForm(this.editForm, product);

    this.memberSubscriptionsSharedCollection =
      this.memberSubscriptionService.addMemberSubscriptionToCollectionIfMissing<IMemberSubscription>(
        this.memberSubscriptionsSharedCollection,
        product.subscription,
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
            this.product?.subscription,
          ),
        ),
      )
      .subscribe((memberSubscriptions: IMemberSubscription[]) => (this.memberSubscriptionsSharedCollection = memberSubscriptions));
  }
}
