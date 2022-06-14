import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IVehicleListing, VehicleListing } from '../vehicle-listing.model';
import { VehicleListingService } from '../service/vehicle-listing.service';

@Injectable({ providedIn: 'root' })
export class VehicleListingRoutingResolveService implements Resolve<IVehicleListing> {
  constructor(protected service: VehicleListingService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IVehicleListing> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((vehicleListing: HttpResponse<VehicleListing>) => {
          if (vehicleListing.body) {
            return of(vehicleListing.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new VehicleListing());
  }
}
