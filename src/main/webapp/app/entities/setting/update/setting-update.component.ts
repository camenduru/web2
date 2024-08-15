import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { Membership } from 'app/entities/enumerations/membership.model';
import { ISetting } from '../setting.model';
import { SettingService } from '../service/setting.service';
import { SettingFormService, SettingFormGroup } from './setting-form.service';

@Component({
  standalone: true,
  selector: 'jhi-setting-update',
  templateUrl: './setting-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class SettingUpdateComponent implements OnInit {
  isSaving = false;
  setting: ISetting | null = null;
  membershipValues = Object.keys(Membership);

  protected settingService = inject(SettingService);
  protected settingFormService = inject(SettingFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: SettingFormGroup = this.settingFormService.createSettingFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ setting }) => {
      this.setting = setting;
      if (setting) {
        this.updateForm(setting);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const setting = this.settingFormService.getSetting(this.editForm);
    if (setting.id !== null) {
      this.subscribeToSaveResponse(this.settingService.update(setting));
    } else {
      this.subscribeToSaveResponse(this.settingService.create(setting));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISetting>>): void {
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

  protected updateForm(setting: ISetting): void {
    this.setting = setting;
    this.settingFormService.resetForm(this.editForm, setting);
  }
}
