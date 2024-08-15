import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IApp } from '../app.model';

@Component({
  standalone: true,
  selector: 'jhi-app-detail',
  templateUrl: './app-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class AppDetailComponent {
  app = input<IApp | null>(null);

  previousState(): void {
    window.history.back();
  }
}
