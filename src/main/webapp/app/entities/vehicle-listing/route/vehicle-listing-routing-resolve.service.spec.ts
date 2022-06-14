import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { IVehicleListing, VehicleListing } from '../vehicle-listing.model';
import { VehicleListingService } from '../service/vehicle-listing.service';

import { VehicleListingRoutingResolveService } from './vehicle-listing-routing-resolve.service';

describe('VehicleListing routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: VehicleListingRoutingResolveService;
  let service: VehicleListingService;
  let resultVehicleListing: IVehicleListing | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      providers: [
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
    routingResolveService = TestBed.inject(VehicleListingRoutingResolveService);
    service = TestBed.inject(VehicleListingService);
    resultVehicleListing = undefined;
  });

  describe('resolve', () => {
    it('should return IVehicleListing returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultVehicleListing = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultVehicleListing).toEqual({ id: 123 });
    });

    it('should return new IVehicleListing if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultVehicleListing = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultVehicleListing).toEqual(new VehicleListing());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as VehicleListing })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultVehicleListing = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultVehicleListing).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
