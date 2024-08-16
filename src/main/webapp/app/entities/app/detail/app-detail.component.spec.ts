jest.mock('app/core/auth/account.service');

import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';
import { Router } from '@angular/router';
import { of, Subject } from 'rxjs';
import { provideHttpClient } from '@angular/common/http';
import { RouterModule } from '@angular/router';

import { AccountService } from 'app/core/auth/account.service';
import { Account } from 'app/core/auth/account.model';
import { TranslateModule, TranslateService, MissingTranslationHandler } from '@ngx-translate/core';
import { missingTranslationHandler } from 'app/config/translation.config';

import { AppDetailComponent } from './app-detail.component';

describe('App Detail Component', () => {
  let comp: AppDetailComponent;
  let fixture: ComponentFixture<AppDetailComponent>;
  let mockAccountService: AccountService;
  let mockRouter: Router;
  const account: Account = {
    activated: true,
    authorities: [],
    email: '',
    firstName: null,
    langKey: '',
    lastName: null,
    login: 'login',
    imageUrl: null,
  };

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      imports: [
        AppDetailComponent,
        RouterModule.forRoot([]),
        TranslateModule.forRoot({
          missingTranslationHandler: {
            provide: MissingTranslationHandler,
            useFactory: missingTranslationHandler,
          },
        }),
      ],
      providers: [AccountService, provideHttpClient()],
    })
      .overrideTemplate(AppDetailComponent, '')
      .compileComponents();
    const translateService = TestBed.inject(TranslateService);
    translateService.setDefaultLang('en');
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AppDetailComponent);
    comp = fixture.componentInstance;
    mockAccountService = TestBed.inject(AccountService);
    mockAccountService.identity = jest.fn(() => of(null));
    mockAccountService.getAuthenticationState = jest.fn(() => of(null));

    mockRouter = TestBed.inject(Router);
    jest.spyOn(mockRouter, 'navigate').mockImplementation(() => Promise.resolve(true));
  });

  // describe('ngOnInit', () => {
  //   it('Should synchronize account variable with current account', () => {
  //     // GIVEN
  //     const authenticationState = new Subject<Account | null>();
  //     mockAccountService.getAuthenticationState = jest.fn(() => authenticationState.asObservable());

  //     // WHEN
  //     comp.ngOnInit();

  //     // THEN
  //     expect(comp.account()).toBeNull();

  //     // WHEN
  //     authenticationState.next(account);

  //     // THEN
  //     expect(comp.account()).toEqual(account);

  //     // WHEN
  //     authenticationState.next(null);

  //     // THEN
  //     expect(comp.account()).toBeNull();
  //   });
  // });

  describe('login', () => {
    it('Should navigate to /login on login', () => {
      // WHEN
      comp.login();

      // THEN
      expect(mockRouter.navigate).toHaveBeenCalledWith(['/login']);
    });
  });

  // describe('ngOnDestroy', () => {
  //   it('Should destroy authentication state subscription on component destroy', () => {
  //     // GIVEN
  //     const authenticationState = new Subject<Account | null>();
  //     mockAccountService.getAuthenticationState = jest.fn(() => authenticationState.asObservable());

  //     // WHEN
  //     comp.ngOnInit();

  //     // THEN
  //     expect(comp.account()).toBeNull();

  //     // WHEN
  //     authenticationState.next(account);

  //     // THEN
  //     expect(comp.account()).toEqual(account);

  //     // WHEN
  //     comp.ngOnDestroy();
  //     authenticationState.next(null);

  //     // THEN
  //     expect(comp.account()).toEqual(account);
  //   });
  // });
});
