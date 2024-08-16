import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import HasAnyAuthorityDirective from 'app/shared/auth/has-any-authority.directive';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { ISetting } from '../setting.model';

@Component({
  standalone: true,
  selector: 'jhi-setting-detail',
  templateUrl: './setting-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe, HasAnyAuthorityDirective],
})
export class SettingDetailComponent {
  setting = input<ISetting | null>(null);

  previousState(): void {
    window.history.back();
  }
}
