import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideHttpClient, HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject, from } from 'rxjs';

import { AppService } from '../service/app.service';
import { IApp } from '../app.model';
import { AppFormService } from './app-form.service';

import { AppUpdateComponent } from './app-update.component';

describe('App Management Update Component', () => {
  let comp: AppUpdateComponent;
  let fixture: ComponentFixture<AppUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let appFormService: AppFormService;
  let appService: AppService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [AppUpdateComponent],
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
      .overrideTemplate(AppUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(AppUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    appFormService = TestBed.inject(AppFormService);
    appService = TestBed.inject(AppService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const app: IApp = { id: 'CBA' };

      activatedRoute.data = of({ app });
      comp.ngOnInit();

      expect(comp.app).toEqual(app);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IApp>>();
      const app = { id: 'ABC' };
      jest.spyOn(appFormService, 'getApp').mockReturnValue(app);
      jest.spyOn(appService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ app });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: app }));
      saveSubject.complete();

      // THEN
      expect(appFormService.getApp).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(appService.update).toHaveBeenCalledWith(expect.objectContaining(app));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IApp>>();
      const app = { id: 'ABC' };
      jest.spyOn(appFormService, 'getApp').mockReturnValue({ id: null });
      jest.spyOn(appService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ app: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: app }));
      saveSubject.complete();

      // THEN
      expect(appFormService.getApp).toHaveBeenCalled();
      expect(appService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IApp>>();
      const app = { id: 'ABC' };
      jest.spyOn(appService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ app });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(appService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
