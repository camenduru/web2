import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideHttpClient, HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject, from } from 'rxjs';

import { RedeemService } from '../service/redeem.service';
import { IRedeem } from '../redeem.model';
import { RedeemFormService } from './redeem-form.service';

import { RedeemUpdateComponent } from './redeem-update.component';

describe('Redeem Management Update Component', () => {
  let comp: RedeemUpdateComponent;
  let fixture: ComponentFixture<RedeemUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let redeemFormService: RedeemFormService;
  let redeemService: RedeemService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [RedeemUpdateComponent],
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
      .overrideTemplate(RedeemUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(RedeemUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    redeemFormService = TestBed.inject(RedeemFormService);
    redeemService = TestBed.inject(RedeemService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const redeem: IRedeem = { id: 'CBA' };

      activatedRoute.data = of({ redeem });
      comp.ngOnInit();

      expect(comp.redeem).toEqual(redeem);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IRedeem>>();
      const redeem = { id: 'ABC' };
      jest.spyOn(redeemFormService, 'getRedeem').mockReturnValue(redeem);
      jest.spyOn(redeemService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ redeem });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: redeem }));
      saveSubject.complete();

      // THEN
      expect(redeemFormService.getRedeem).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(redeemService.update).toHaveBeenCalledWith(expect.objectContaining(redeem));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IRedeem>>();
      const redeem = { id: 'ABC' };
      jest.spyOn(redeemFormService, 'getRedeem').mockReturnValue({ id: null });
      jest.spyOn(redeemService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ redeem: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: redeem }));
      saveSubject.complete();

      // THEN
      expect(redeemFormService.getRedeem).toHaveBeenCalled();
      expect(redeemService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IRedeem>>();
      const redeem = { id: 'ABC' };
      jest.spyOn(redeemService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ redeem });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(redeemService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
