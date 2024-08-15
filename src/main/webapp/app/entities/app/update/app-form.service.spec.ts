import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../app.test-samples';

import { AppFormService } from './app-form.service';

describe('App Form Service', () => {
  let service: AppFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AppFormService);
  });

  describe('Service methods', () => {
    describe('createAppFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createAppFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            type: expect.any(Object),
            amount: expect.any(Object),
            schema: expect.any(Object),
            model: expect.any(Object),
            title: expect.any(Object),
            isDefault: expect.any(Object),
            isActive: expect.any(Object),
            isFree: expect.any(Object),
          }),
        );
      });

      it('passing IApp should create a new form with FormGroup', () => {
        const formGroup = service.createAppFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            type: expect.any(Object),
            amount: expect.any(Object),
            schema: expect.any(Object),
            model: expect.any(Object),
            title: expect.any(Object),
            isDefault: expect.any(Object),
            isActive: expect.any(Object),
            isFree: expect.any(Object),
          }),
        );
      });
    });

    describe('getApp', () => {
      it('should return NewApp for default App initial value', () => {
        const formGroup = service.createAppFormGroup(sampleWithNewData);

        const app = service.getApp(formGroup) as any;

        expect(app).toMatchObject(sampleWithNewData);
      });

      it('should return NewApp for empty App initial value', () => {
        const formGroup = service.createAppFormGroup();

        const app = service.getApp(formGroup) as any;

        expect(app).toMatchObject({});
      });

      it('should return IApp', () => {
        const formGroup = service.createAppFormGroup(sampleWithRequiredData);

        const app = service.getApp(formGroup) as any;

        expect(app).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IApp should not enable id FormControl', () => {
        const formGroup = service.createAppFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewApp should disable id FormControl', () => {
        const formGroup = service.createAppFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
