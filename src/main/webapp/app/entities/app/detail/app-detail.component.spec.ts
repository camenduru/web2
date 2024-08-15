import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { AppDetailComponent } from './app-detail.component';

describe('App Management Detail Component', () => {
  let comp: AppDetailComponent;
  let fixture: ComponentFixture<AppDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AppDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: AppDetailComponent,
              resolve: { app: () => of({ id: 'ABC' }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(AppDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AppDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load app on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', AppDetailComponent);

      // THEN
      expect(instance.app()).toEqual(expect.objectContaining({ id: 'ABC' }));
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
