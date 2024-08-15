import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { SettingDetailComponent } from './setting-detail.component';

describe('Setting Management Detail Component', () => {
  let comp: SettingDetailComponent;
  let fixture: ComponentFixture<SettingDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SettingDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: SettingDetailComponent,
              resolve: { setting: () => of({ id: 'ABC' }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(SettingDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(SettingDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load setting on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', SettingDetailComponent);

      // THEN
      expect(instance.setting()).toEqual(expect.objectContaining({ id: 'ABC' }));
    });
  });

  describe('PreviousState', () => {
    it('Should navigate to previous state', () => {
      jest.spyOn(window.history, 'back');
      comp.previousState();
      expect(window.history.back).toHaveBeenCalled();
    });
  });
});
