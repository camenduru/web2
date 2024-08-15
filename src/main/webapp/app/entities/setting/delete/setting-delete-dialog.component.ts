import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { ISetting } from '../setting.model';
import { SettingService } from '../service/setting.service';

@Component({
  standalone: true,
  templateUrl: './setting-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class SettingDeleteDialogComponent {
  setting?: ISetting;

  protected settingService = inject(SettingService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: string): void {
    this.settingService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
