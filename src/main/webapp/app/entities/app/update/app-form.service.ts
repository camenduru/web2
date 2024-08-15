import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IApp, NewApp } from '../app.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IApp for edit and NewAppFormGroupInput for create.
 */
type AppFormGroupInput = IApp | PartialWithRequiredKeyOf<NewApp>;

type AppFormDefaults = Pick<NewApp, 'id' | 'isDefault' | 'isActive' | 'isFree'>;

type AppFormGroupContent = {
  id: FormControl<IApp['id'] | NewApp['id']>;
  type: FormControl<IApp['type']>;
  amount: FormControl<IApp['amount']>;
  schema: FormControl<IApp['schema']>;
  model: FormControl<IApp['model']>;
  title: FormControl<IApp['title']>;
  isDefault: FormControl<IApp['isDefault']>;
  isActive: FormControl<IApp['isActive']>;
  isFree: FormControl<IApp['isFree']>;
};

export type AppFormGroup = FormGroup<AppFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class AppFormService {
  createAppFormGroup(app: AppFormGroupInput = { id: null }): AppFormGroup {
    const appRawValue = {
      ...this.getFormDefaults(),
      ...app,
    };
    return new FormGroup<AppFormGroupContent>({
      id: new FormControl(
        { value: appRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      type: new FormControl(appRawValue.type, {
        validators: [Validators.required],
      }),
      amount: new FormControl(appRawValue.amount, {
        validators: [Validators.required],
      }),
      schema: new FormControl(appRawValue.schema, {
        validators: [Validators.required],
      }),
      model: new FormControl(appRawValue.model, {
        validators: [Validators.required],
      }),
      title: new FormControl(appRawValue.title, {
        validators: [Validators.required],
      }),
      isDefault: new FormControl(appRawValue.isDefault, {
        validators: [Validators.required],
      }),
      isActive: new FormControl(appRawValue.isActive, {
        validators: [Validators.required],
      }),
      isFree: new FormControl(appRawValue.isFree, {
        validators: [Validators.required],
      }),
    });
  }

  getApp(form: AppFormGroup): IApp | NewApp {
    return form.getRawValue() as IApp | NewApp;
  }

  resetForm(form: AppFormGroup, app: AppFormGroupInput): void {
    const appRawValue = { ...this.getFormDefaults(), ...app };
    form.reset(
      {
        ...appRawValue,
        id: { value: appRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): AppFormDefaults {
    return {
      id: null,
      isDefault: false,
      isActive: false,
      isFree: false,
    };
  }
}
