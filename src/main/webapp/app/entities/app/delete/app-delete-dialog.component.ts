import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IApp } from '../app.model';
import { AppService } from '../service/app.service';

@Component({
  standalone: true,
  templateUrl: './app-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class AppDeleteDialogComponent {
  app?: IApp;

  protected appService = inject(AppService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: string): void {
    this.appService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
