import { TestBed } from '@angular/core/testing';
import { provideHttpClient, HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { of } from 'rxjs';

import { IApp } from '../app.model';
import { AppService } from '../service/app.service';

import appResolve from './app-routing-resolve.service';

describe('App routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let service: AppService;
  let resultApp: IApp | null | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        provideHttpClient(),
        {
          provide: ActivatedRoute,
          useValue: {
            snapshot: {
              paramMap: convertToParamMap({}),
            },
          },
        },
      ],
    });
    mockRouter = TestBed.inject(Router);
    jest.spyOn(mockRouter, 'navigate').mockImplementation(() => Promise.resolve(true));
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRoute).snapshot;
    service = TestBed.inject(AppService);
    resultApp = undefined;
  });

  describe('resolve', () => {
    it('should return IApp returned by find for valid MongoDB ObjectId', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: '123456789012345678901234' };

      // WHEN
      TestBed.runInInjectionContext(() => {
        appResolve(mockActivatedRouteSnapshot).subscribe({
          next(result) {
            resultApp = result;
          },
        });
      });

      // THEN
      expect(service.find).toHaveBeenCalledWith('123456789012345678901234');
      expect(resultApp).toEqual({ id: '123456789012345678901234' });
    });

    it('should return IApp returned by findByType for non-MongoDB ObjectId', () => {
      // GIVEN
      service.findByType = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 'ABC' };

      // WHEN
      TestBed.runInInjectionContext(() => {
        appResolve(mockActivatedRouteSnapshot).subscribe({
          next(result) {
            resultApp = result;
          },
        });
      });

      // THEN
      expect(service.findByType).toHaveBeenCalledWith('ABC');
      expect(resultApp).toEqual({ id: 'ABC' });
    });

    it('should return null if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      TestBed.runInInjectionContext(() => {
        appResolve(mockActivatedRouteSnapshot).subscribe({
          next(result) {
            resultApp = result;
          },
        });
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultApp).toEqual(null);
    });

    it('should route to 404 page if data not found in server for valid MongoDB ObjectId', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse<IApp>({ body: null })));
      mockActivatedRouteSnapshot.params = { id: '123456789012345678901234' };

      // WHEN
      TestBed.runInInjectionContext(() => {
        appResolve(mockActivatedRouteSnapshot).subscribe({
          next(result) {
            resultApp = result;
          },
        });
      });

      // THEN
      expect(service.find).toHaveBeenCalledWith('123456789012345678901234');
      expect(resultApp).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });

    it('should route to 404 page if data not found in server for non-MongoDB ObjectId', () => {
      // GIVEN
      jest.spyOn(service, 'findByType').mockReturnValue(of(new HttpResponse<IApp>({ body: null })));
      mockActivatedRouteSnapshot.params = { id: 'ABC' };

      // WHEN
      TestBed.runInInjectionContext(() => {
        appResolve(mockActivatedRouteSnapshot).subscribe({
          next(result) {
            resultApp = result;
          },
        });
      });

      // THEN
      expect(service.findByType).toHaveBeenCalledWith('ABC');
      expect(resultApp).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
