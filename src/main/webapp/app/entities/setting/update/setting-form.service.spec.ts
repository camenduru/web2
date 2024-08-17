import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../setting.test-samples';

import { SettingFormService } from './setting-form.service';

describe('Setting Form Service', () => {
  let service: SettingFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SettingFormService);
  });

  describe('Service methods', () => {
    describe('createSettingFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createSettingFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            login: expect.any(Object),
            total: expect.any(Object),
            membership: expect.any(Object),
            notifyUri: expect.any(Object),
            notifyToken: expect.any(Object),
            discordUsername: expect.any(Object),
            discordId: expect.any(Object),
            discordChannel: expect.any(Object),
            discordToken: expect.any(Object),
            apiKey: expect.any(Object),
          }),
        );
      });

      it('passing ISetting should create a new form with FormGroup', () => {
        const formGroup = service.createSettingFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            login: expect.any(Object),
            total: expect.any(Object),
            membership: expect.any(Object),
            notifyUri: expect.any(Object),
            notifyToken: expect.any(Object),
            discordUsername: expect.any(Object),
            discordId: expect.any(Object),
            discordChannel: expect.any(Object),
            discordToken: expect.any(Object),
            apiKey: expect.any(Object),
          }),
        );
      });
    });

    describe('getSetting', () => {
      it('should return NewSetting for default Setting initial value', () => {
        const formGroup = service.createSettingFormGroup(sampleWithNewData);

        const setting = service.getSetting(formGroup) as any;

        expect(setting).toMatchObject(sampleWithNewData);
      });

      it('should return NewSetting for empty Setting initial value', () => {
        const formGroup = service.createSettingFormGroup();

        const setting = service.getSetting(formGroup) as any;

        expect(setting).toMatchObject({});
      });

      it('should return ISetting', () => {
        const formGroup = service.createSettingFormGroup(sampleWithRequiredData);

        const setting = service.getSetting(formGroup) as any;

        expect(setting).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ISetting should not enable id FormControl', () => {
        const formGroup = service.createSettingFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewSetting should disable id FormControl', () => {
        const formGroup = service.createSettingFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
