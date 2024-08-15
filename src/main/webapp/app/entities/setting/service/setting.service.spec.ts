import { TestBed } from '@angular/core/testing';
import { provideHttpClientTesting, HttpTestingController } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { ISetting } from '../setting.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../setting.test-samples';

import { SettingService } from './setting.service';

const requireRestSample: ISetting = {
  ...sampleWithRequiredData,
};

describe('Setting Service', () => {
  let service: SettingService;
  let httpMock: HttpTestingController;
  let expectedResult: ISetting | ISetting[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(SettingService);
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

    it('should create a Setting', () => {
      const setting = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(setting).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Setting', () => {
      const setting = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(setting).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Setting', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Setting', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Setting', () => {
      const expected = true;

      service.delete('ABC').subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addSettingToCollectionIfMissing', () => {
      it('should add a Setting to an empty array', () => {
        const setting: ISetting = sampleWithRequiredData;
        expectedResult = service.addSettingToCollectionIfMissing([], setting);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(setting);
      });

      it('should not add a Setting to an array that contains it', () => {
        const setting: ISetting = sampleWithRequiredData;
        const settingCollection: ISetting[] = [
          {
            ...setting,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addSettingToCollectionIfMissing(settingCollection, setting);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Setting to an array that doesn't contain it", () => {
        const setting: ISetting = sampleWithRequiredData;
        const settingCollection: ISetting[] = [sampleWithPartialData];
        expectedResult = service.addSettingToCollectionIfMissing(settingCollection, setting);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(setting);
      });

      it('should add only unique Setting to an array', () => {
        const settingArray: ISetting[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const settingCollection: ISetting[] = [sampleWithRequiredData];
        expectedResult = service.addSettingToCollectionIfMissing(settingCollection, ...settingArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const setting: ISetting = sampleWithRequiredData;
        const setting2: ISetting = sampleWithPartialData;
        expectedResult = service.addSettingToCollectionIfMissing([], setting, setting2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(setting);
        expect(expectedResult).toContain(setting2);
      });

      it('should accept null and undefined values', () => {
        const setting: ISetting = sampleWithRequiredData;
        expectedResult = service.addSettingToCollectionIfMissing([], null, setting, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(setting);
      });

      it('should return initial array if no Setting is added', () => {
        const settingCollection: ISetting[] = [sampleWithRequiredData];
        expectedResult = service.addSettingToCollectionIfMissing(settingCollection, undefined, null);
        expect(expectedResult).toEqual(settingCollection);
      });
    });

    describe('compareSetting', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareSetting(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 'ABC' };
        const entity2 = null;

        const compareResult1 = service.compareSetting(entity1, entity2);
        const compareResult2 = service.compareSetting(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 'ABC' };
        const entity2 = { id: 'CBA' };

        const compareResult1 = service.compareSetting(entity1, entity2);
        const compareResult2 = service.compareSetting(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 'ABC' };
        const entity2 = { id: 'ABC' };

        const compareResult1 = service.compareSetting(entity1, entity2);
        const compareResult2 = service.compareSetting(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
