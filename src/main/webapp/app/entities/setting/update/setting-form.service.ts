import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ISetting, NewSetting } from '../setting.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ISetting for edit and NewSettingFormGroupInput for create.
 */
type SettingFormGroupInput = ISetting | PartialWithRequiredKeyOf<NewSetting>;

type SettingFormDefaults = Pick<NewSetting, 'id'>;

type SettingFormGroupContent = {
  id: FormControl<ISetting['id'] | NewSetting['id']>;
  login: FormControl<ISetting['login']>;
  total: FormControl<ISetting['total']>;
  membership: FormControl<ISetting['membership']>;
  notifyUri: FormControl<ISetting['notifyUri']>;
  notifyToken: FormControl<ISetting['notifyToken']>;
  discordUsername: FormControl<ISetting['discordUsername']>;
  discordId: FormControl<ISetting['discordId']>;
  discordChannel: FormControl<ISetting['discordChannel']>;
  discordToken: FormControl<ISetting['discordToken']>;
  apiKey: FormControl<ISetting['apiKey']>;
};

export type SettingFormGroup = FormGroup<SettingFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class SettingFormService {
  createSettingFormGroup(setting: SettingFormGroupInput = { id: null }): SettingFormGroup {
    const settingRawValue = {
      ...this.getFormDefaults(),
      ...setting,
    };
    return new FormGroup<SettingFormGroupContent>({
      id: new FormControl(
        { value: settingRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      login: new FormControl(settingRawValue.login, {
        validators: [Validators.required],
      }),
      total: new FormControl(settingRawValue.total, {
        validators: [Validators.required],
      }),
      membership: new FormControl(settingRawValue.membership, {
        validators: [Validators.required],
      }),
      notifyUri: new FormControl(settingRawValue.notifyUri, {
        validators: [Validators.required],
      }),
      notifyToken: new FormControl(settingRawValue.notifyToken, {
        validators: [Validators.required],
      }),
      discordUsername: new FormControl(settingRawValue.discordUsername, {
        validators: [Validators.required],
      }),
      discordId: new FormControl(settingRawValue.discordId, {
        validators: [Validators.required],
      }),
      discordChannel: new FormControl(settingRawValue.discordChannel, {
        validators: [Validators.required],
      }),
      discordToken: new FormControl(settingRawValue.discordToken, {
        validators: [Validators.required],
      }),
      apiKey: new FormControl(settingRawValue.apiKey, {
        validators: [Validators.required],
      }),
    });
  }

  getSetting(form: SettingFormGroup): ISetting | NewSetting {
    return form.getRawValue() as ISetting | NewSetting;
  }

  resetForm(form: SettingFormGroup, setting: SettingFormGroupInput): void {
    const settingRawValue = { ...this.getFormDefaults(), ...setting };
    form.reset(
      {
        ...settingRawValue,
        id: { value: settingRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): SettingFormDefaults {
    return {
      id: null,
    };
  }
}
