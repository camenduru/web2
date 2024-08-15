import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { ISetting } from '../setting.model';

@Component({
  standalone: true,
  selector: 'jhi-setting-detail',
  templateUrl: './setting-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class SettingDetailComponent {
  setting = input<ISetting | null>(null);

  previousState(): void {
    window.history.back();
  }
}
