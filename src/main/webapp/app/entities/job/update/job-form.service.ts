import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IJob, NewJob } from '../job.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IJob for edit and NewJobFormGroupInput for create.
 */
type JobFormGroupInput = IJob | PartialWithRequiredKeyOf<NewJob>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IJob | NewJob> = Omit<T, 'date'> & {
  date?: string | null;
};

type JobFormRawValue = FormValueOf<IJob>;

type NewJobFormRawValue = FormValueOf<NewJob>;

type JobFormDefaults = Pick<NewJob, 'id' | 'date'>;

type JobFormGroupContent = {
  id: FormControl<JobFormRawValue['id'] | NewJob['id']>;
  login: FormControl<JobFormRawValue['login']>;
  date: FormControl<JobFormRawValue['date']>;
  status: FormControl<JobFormRawValue['status']>;
  type: FormControl<JobFormRawValue['type']>;
  command: FormControl<JobFormRawValue['command']>;
  amount: FormControl<JobFormRawValue['amount']>;
  notifyUri: FormControl<JobFormRawValue['notifyUri']>;
  notifyToken: FormControl<JobFormRawValue['notifyToken']>;
  discordUsername: FormControl<JobFormRawValue['discordUsername']>;
  discordId: FormControl<JobFormRawValue['discordId']>;
  discordChannel: FormControl<JobFormRawValue['discordChannel']>;
  discordToken: FormControl<JobFormRawValue['discordToken']>;
  source: FormControl<JobFormRawValue['source']>;
  total: FormControl<JobFormRawValue['total']>;
  result: FormControl<JobFormRawValue['result']>;
};

export type JobFormGroup = FormGroup<JobFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class JobFormService {
  createJobFormGroup(job: JobFormGroupInput = { id: null }): JobFormGroup {
    const jobRawValue = this.convertJobToJobRawValue({
      ...this.getFormDefaults(),
      ...job,
    });
    return new FormGroup<JobFormGroupContent>({
      id: new FormControl(
        { value: jobRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      login: new FormControl(jobRawValue.login, {
        validators: [Validators.required],
      }),
      date: new FormControl(jobRawValue.date, {
        validators: [Validators.required],
      }),
      status: new FormControl(jobRawValue.status, {
        validators: [Validators.required],
      }),
      type: new FormControl(jobRawValue.type, {
        validators: [Validators.required],
      }),
      command: new FormControl(jobRawValue.command, {
        validators: [Validators.required],
      }),
      amount: new FormControl(jobRawValue.amount, {
        validators: [Validators.required],
      }),
      notifyUri: new FormControl(jobRawValue.notifyUri, {
        validators: [Validators.required],
      }),
      notifyToken: new FormControl(jobRawValue.notifyToken, {
        validators: [Validators.required],
      }),
      discordUsername: new FormControl(jobRawValue.discordUsername, {
        validators: [Validators.required],
      }),
      discordId: new FormControl(jobRawValue.discordId, {
        validators: [Validators.required],
      }),
      discordChannel: new FormControl(jobRawValue.discordChannel, {
        validators: [Validators.required],
      }),
      discordToken: new FormControl(jobRawValue.discordToken, {
        validators: [Validators.required],
      }),
      source: new FormControl(jobRawValue.source, {
        validators: [Validators.required],
      }),
      total: new FormControl(jobRawValue.total, {
        validators: [Validators.required],
      }),
      result: new FormControl(jobRawValue.result, {
        validators: [Validators.required],
      }),
    });
  }

  getJob(form: JobFormGroup): IJob | NewJob {
    return this.convertJobRawValueToJob(form.getRawValue() as JobFormRawValue | NewJobFormRawValue);
  }

  resetForm(form: JobFormGroup, job: JobFormGroupInput): void {
    const jobRawValue = this.convertJobToJobRawValue({ ...this.getFormDefaults(), ...job });
    form.reset(
      {
        ...jobRawValue,
        id: { value: jobRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): JobFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      date: currentTime,
    };
  }

  private convertJobRawValueToJob(rawJob: JobFormRawValue | NewJobFormRawValue): IJob | NewJob {
    return {
      ...rawJob,
      date: dayjs(rawJob.date, DATE_TIME_FORMAT),
    };
  }

  private convertJobToJobRawValue(
    job: IJob | (Partial<NewJob> & JobFormDefaults),
  ): JobFormRawValue | PartialWithRequiredKeyOf<NewJobFormRawValue> {
    return {
      ...job,
      date: job.date ? job.date.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
