import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { FuelType } from 'app/entities/enumerations/fuel-type.model';
import { ListingStatus } from 'app/entities/enumerations/listing-status.model';
import { IVehicleListing, VehicleListing } from '../vehicle-listing.model';

import { VehicleListingService } from './vehicle-listing.service';

describe('VehicleListing Service', () => {
  let service: VehicleListingService;
  let httpMock: HttpTestingController;
  let elemDefault: IVehicleListing;
  let expectedResult: IVehicleListing | IVehicleListing[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(VehicleListingService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      price: 0,
      year: 0,
      mileage: 0,
      fuel: FuelType.DIESEL,
      status: ListingStatus.PENDING,
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

    it('should create a VehicleListing', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new VehicleListing()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a VehicleListing', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          price: 1,
          year: 1,
          mileage: 1,
          fuel: 'BBBBBB',
          status: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a VehicleListing', () => {
      const patchObject = Object.assign(
        {
          mileage: 1,
          fuel: 'BBBBBB',
        },
        new VehicleListing()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of VehicleListing', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          price: 1,
          year: 1,
          mileage: 1,
          fuel: 'BBBBBB',
          status: 'BBBBBB',
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

    it('should delete a VehicleListing', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addVehicleListingToCollectionIfMissing', () => {
      it('should add a VehicleListing to an empty array', () => {
        const vehicleListing: IVehicleListing = { id: 123 };
        expectedResult = service.addVehicleListingToCollectionIfMissing([], vehicleListing);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(vehicleListing);
      });

      it('should not add a VehicleListing to an array that contains it', () => {
        const vehicleListing: IVehicleListing = { id: 123 };
        const vehicleListingCollection: IVehicleListing[] = [
          {
            ...vehicleListing,
          },
          { id: 456 },
        ];
        expectedResult = service.addVehicleListingToCollectionIfMissing(vehicleListingCollection, vehicleListing);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a VehicleListing to an array that doesn't contain it", () => {
        const vehicleListing: IVehicleListing = { id: 123 };
        const vehicleListingCollection: IVehicleListing[] = [{ id: 456 }];
        expectedResult = service.addVehicleListingToCollectionIfMissing(vehicleListingCollection, vehicleListing);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(vehicleListing);
      });

      it('should add only unique VehicleListing to an array', () => {
        const vehicleListingArray: IVehicleListing[] = [{ id: 123 }, { id: 456 }, { id: 10044 }];
        const vehicleListingCollection: IVehicleListing[] = [{ id: 123 }];
        expectedResult = service.addVehicleListingToCollectionIfMissing(vehicleListingCollection, ...vehicleListingArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const vehicleListing: IVehicleListing = { id: 123 };
        const vehicleListing2: IVehicleListing = { id: 456 };
        expectedResult = service.addVehicleListingToCollectionIfMissing([], vehicleListing, vehicleListing2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(vehicleListing);
        expect(expectedResult).toContain(vehicleListing2);
      });

      it('should accept null and undefined values', () => {
        const vehicleListing: IVehicleListing = { id: 123 };
        expectedResult = service.addVehicleListingToCollectionIfMissing([], null, vehicleListing, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(vehicleListing);
      });

      it('should return initial array if no VehicleListing is added', () => {
        const vehicleListingCollection: IVehicleListing[] = [{ id: 123 }];
        expectedResult = service.addVehicleListingToCollectionIfMissing(vehicleListingCollection, undefined, null);
        expect(expectedResult).toEqual(vehicleListingCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
