import { TestBed } from '@angular/core/testing';
import { provideHttpClientTesting, HttpTestingController } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IApp } from '../app.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../app.test-samples';

import { AppService } from './app.service';

const requireRestSample: IApp = {
  ...sampleWithRequiredData,
};

describe('App Service', () => {
  let service: AppService;
  let httpMock: HttpTestingController;
  let expectedResult: IApp | IApp[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(AppService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find('ABC').subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a App', () => {
      const app = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(app).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a App', () => {
      const app = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(app).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a App', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of App', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a App', () => {
      const expected = true;

      service.delete('ABC').subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addAppToCollectionIfMissing', () => {
      it('should add a App to an empty array', () => {
        const app: IApp = sampleWithRequiredData;
        expectedResult = service.addAppToCollectionIfMissing([], app);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(app);
      });

      it('should not add a App to an array that contains it', () => {
        const app: IApp = sampleWithRequiredData;
        const appCollection: IApp[] = [
          {
            ...app,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addAppToCollectionIfMissing(appCollection, app);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a App to an array that doesn't contain it", () => {
        const app: IApp = sampleWithRequiredData;
        const appCollection: IApp[] = [sampleWithPartialData];
        expectedResult = service.addAppToCollectionIfMissing(appCollection, app);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(app);
      });

      it('should add only unique App to an array', () => {
        const appArray: IApp[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const appCollection: IApp[] = [sampleWithRequiredData];
        expectedResult = service.addAppToCollectionIfMissing(appCollection, ...appArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const app: IApp = sampleWithRequiredData;
        const app2: IApp = sampleWithPartialData;
        expectedResult = service.addAppToCollectionIfMissing([], app, app2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(app);
        expect(expectedResult).toContain(app2);
      });

      it('should accept null and undefined values', () => {
        const app: IApp = sampleWithRequiredData;
        expectedResult = service.addAppToCollectionIfMissing([], null, app, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(app);
      });

      it('should return initial array if no App is added', () => {
        const appCollection: IApp[] = [sampleWithRequiredData];
        expectedResult = service.addAppToCollectionIfMissing(appCollection, undefined, null);
        expect(expectedResult).toEqual(appCollection);
      });
    });

    describe('compareApp', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareApp(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 'ABC' };
        const entity2 = null;

        const compareResult1 = service.compareApp(entity1, entity2);
        const compareResult2 = service.compareApp(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 'ABC' };
        const entity2 = { id: 'CBA' };

        const compareResult1 = service.compareApp(entity1, entity2);
        const compareResult2 = service.compareApp(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 'ABC' };
        const entity2 = { id: 'ABC' };

        const compareResult1 = service.compareApp(entity1, entity2);
        const compareResult2 = service.compareApp(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
