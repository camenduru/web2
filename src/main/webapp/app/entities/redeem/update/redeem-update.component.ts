import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { RedeemStatus } from 'app/entities/enumerations/redeem-status.model';
import { IRedeem } from '../redeem.model';
import { RedeemService } from '../service/redeem.service';
import { RedeemFormService, RedeemFormGroup } from './redeem-form.service';

@Component({
  standalone: true,
  selector: 'jhi-redeem-update',
  templateUrl: './redeem-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class RedeemUpdateComponent implements OnInit {
  isSaving = false;
  redeem: IRedeem | null = null;
  redeemStatusValues = Object.keys(RedeemStatus);

  protected redeemService = inject(RedeemService);
  protected redeemFormService = inject(RedeemFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: RedeemFormGroup = this.redeemFormService.createRedeemFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ redeem }) => {
      this.redeem = redeem;
      if (redeem) {
        this.updateForm(redeem);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const redeem = this.redeemFormService.getRedeem(this.editForm);
    if (redeem.id !== null) {
      this.subscribeToSaveResponse(this.redeemService.update(redeem));
    } else {
      this.subscribeToSaveResponse(this.redeemService.create(redeem));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IRedeem>>): void {
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

  protected updateForm(redeem: IRedeem): void {
    this.redeem = redeem;
    this.redeemFormService.resetForm(this.editForm, redeem);
  }
}
