import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ICarModel, CarModel } from '../car-model.model';

import { CarModelService } from './car-model.service';

describe('CarModel Service', () => {
  let service: CarModelService;
  let httpMock: HttpTestingController;
  let elemDefault: ICarModel;
  let expectedResult: ICarModel | ICarModel[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(CarModelService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      make: 'AAAAAAA',
      model: 'AAAAAAA',
      launchYear: 0,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign({}, elemDefault);

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a CarModel', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new CarModel()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a CarModel', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          make: 'BBBBBB',
          model: 'BBBBBB',
          launchYear: 1,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a CarModel', () => {
      const patchObject = Object.assign({}, new CarModel());

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of CarModel', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          make: 'BBBBBB',
          model: 'BBBBBB',
          launchYear: 1,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a CarModel', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addCarModelToCollectionIfMissing', () => {
      it('should add a CarModel to an empty array', () => {
        const carModel: ICarModel = { id: 123 };
        expectedResult = service.addCarModelToCollectionIfMissing([], carModel);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(carModel);
      });

      it('should not add a CarModel to an array that contains it', () => {
        const carModel: ICarModel = { id: 123 };
        const carModelCollection: ICarModel[] = [
          {
            ...carModel,
          },
          { id: 456 },
        ];
        expectedResult = service.addCarModelToCollectionIfMissing(carModelCollection, carModel);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a CarModel to an array that doesn't contain it", () => {
        const carModel: ICarModel = { id: 123 };
        const carModelCollection: ICarModel[] = [{ id: 456 }];
        expectedResult = service.addCarModelToCollectionIfMissing(carModelCollection, carModel);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(carModel);
      });

      it('should add only unique CarModel to an array', () => {
        const carModelArray: ICarModel[] = [{ id: 123 }, { id: 456 }, { id: 90147 }];
        const carModelCollection: ICarModel[] = [{ id: 123 }];
        expectedResult = service.addCarModelToCollectionIfMissing(carModelCollection, ...carModelArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const carModel: ICarModel = { id: 123 };
        const carModel2: ICarModel = { id: 456 };
        expectedResult = service.addCarModelToCollectionIfMissing([], carModel, carModel2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(carModel);
        expect(expectedResult).toContain(carModel2);
      });

      it('should accept null and undefined values', () => {
        const carModel: ICarModel = { id: 123 };
        expectedResult = service.addCarModelToCollectionIfMissing([], null, carModel, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(carModel);
      });

      it('should return initial array if no CarModel is added', () => {
        const carModelCollection: ICarModel[] = [{ id: 123 }];
        expectedResult = service.addCarModelToCollectionIfMissing(carModelCollection, undefined, null);
        expect(expectedResult).toEqual(carModelCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
