import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IMemberSubscription } from 'app/entities/member-subscription/member-subscription.model';
import { MemberSubscriptionService } from 'app/entities/member-subscription/service/member-subscription.service';
import { ProductService } from '../service/product.service';
import { IProduct } from '../product.model';
import { ProductFormService } from './product-form.service';

import { ProductUpdateComponent } from './product-update.component';

describe('Product Management Update Component', () => {
  let comp: ProductUpdateComponent;
  let fixture: ComponentFixture<ProductUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let productFormService: ProductFormService;
  let productService: ProductService;
  let memberSubscriptionService: MemberSubscriptionService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [ProductUpdateComponent],
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
      .overrideTemplate(ProductUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ProductUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    productFormService = TestBed.inject(ProductFormService);
    productService = TestBed.inject(ProductService);
    memberSubscriptionService = TestBed.inject(MemberSubscriptionService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call MemberSubscription query and add missing value', () => {
      const product: IProduct = { id: 456 };
      const subscription: IMemberSubscription = { id: 31271 };
      product.subscription = subscription;

      const memberSubscriptionCollection: IMemberSubscription[] = [{ id: 11205 }];
      jest.spyOn(memberSubscriptionService, 'query').mockReturnValue(of(new HttpResponse({ body: memberSubscriptionCollection })));
      const additionalMemberSubscriptions = [subscription];
      const expectedCollection: IMemberSubscription[] = [...additionalMemberSubscriptions, ...memberSubscriptionCollection];
      jest.spyOn(memberSubscriptionService, 'addMemberSubscriptionToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ product });
      comp.ngOnInit();

      expect(memberSubscriptionService.query).toHaveBeenCalled();
      expect(memberSubscriptionService.addMemberSubscriptionToCollectionIfMissing).toHaveBeenCalledWith(
        memberSubscriptionCollection,
        ...additionalMemberSubscriptions.map(expect.objectContaining),
      );
      expect(comp.memberSubscriptionsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const product: IProduct = { id: 456 };
      const subscription: IMemberSubscription = { id: 18301 };
      product.subscription = subscription;

      activatedRoute.data = of({ product });
      comp.ngOnInit();

      expect(comp.memberSubscriptionsSharedCollection).toContain(subscription);
      expect(comp.product).toEqual(product);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IProduct>>();
      const product = { id: 123 };
      jest.spyOn(productFormService, 'getProduct').mockReturnValue(product);
      jest.spyOn(productService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ product });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: product }));
      saveSubject.complete();

      // THEN
      expect(productFormService.getProduct).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(productService.update).toHaveBeenCalledWith(expect.objectContaining(product));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IProduct>>();
      const product = { id: 123 };
      jest.spyOn(productFormService, 'getProduct').mockReturnValue({ id: null });
      jest.spyOn(productService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ product: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: product }));
      saveSubject.complete();

      // THEN
      expect(productFormService.getProduct).toHaveBeenCalled();
      expect(productService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IProduct>>();
      const product = { id: 123 };
      jest.spyOn(productService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ product });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(productService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareMemberSubscription', () => {
      it('Should forward to memberSubscriptionService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(memberSubscriptionService, 'compareMemberSubscription');
        comp.compareMemberSubscription(entity, entity2);
        expect(memberSubscriptionService.compareMemberSubscription).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
