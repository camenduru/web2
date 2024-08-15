import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IApp } from '../app.model';
import { AppService } from '../service/app.service';
import { AppFormService, AppFormGroup } from './app-form.service';

@Component({
  standalone: true,
  selector: 'jhi-app-update',
  templateUrl: './app-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class AppUpdateComponent implements OnInit {
  isSaving = false;
  app: IApp | null = null;

  protected appService = inject(AppService);
  protected appFormService = inject(AppFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: AppFormGroup = this.appFormService.createAppFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ app }) => {
      this.app = app;
      if (app) {
        this.updateForm(app);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const app = this.appFormService.getApp(this.editForm);
    if (app.id !== null) {
      this.subscribeToSaveResponse(this.appService.update(app));
    } else {
      this.subscribeToSaveResponse(this.appService.create(app));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IApp>>): void {
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

  protected updateForm(app: IApp): void {
    this.app = app;
    this.appFormService.resetForm(this.editForm, app);
  }
}
