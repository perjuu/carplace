import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IVehicleListing, getVehicleListingIdentifier } from '../vehicle-listing.model';

export type EntityResponseType = HttpResponse<IVehicleListing>;
export type EntityArrayResponseType = HttpResponse<IVehicleListing[]>;

@Injectable({ providedIn: 'root' })
export class VehicleListingService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/vehicle-listings');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(vehicleListing: IVehicleListing): Observable<EntityResponseType> {
    return this.http.post<IVehicleListing>(this.resourceUrl, vehicleListing, { observe: 'response' });
  }

  update(vehicleListing: IVehicleListing): Observable<EntityResponseType> {
    return this.http.put<IVehicleListing>(`${this.resourceUrl}/${getVehicleListingIdentifier(vehicleListing) as number}`, vehicleListing, {
      observe: 'response',
    });
  }

  partialUpdate(vehicleListing: IVehicleListing): Observable<EntityResponseType> {
    return this.http.patch<IVehicleListing>(
      `${this.resourceUrl}/${getVehicleListingIdentifier(vehicleListing) as number}`,
      vehicleListing,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IVehicleListing>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IVehicleListing[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addVehicleListingToCollectionIfMissing(
    vehicleListingCollection: IVehicleListing[],
    ...vehicleListingsToCheck: (IVehicleListing | null | undefined)[]
  ): IVehicleListing[] {
    const vehicleListings: IVehicleListing[] = vehicleListingsToCheck.filter(isPresent);
    if (vehicleListings.length > 0) {
      const vehicleListingCollectionIdentifiers = vehicleListingCollection.map(
        vehicleListingItem => getVehicleListingIdentifier(vehicleListingItem)!
      );
      const vehicleListingsToAdd = vehicleListings.filter(vehicleListingItem => {
        const vehicleListingIdentifier = getVehicleListingIdentifier(vehicleListingItem);
        if (vehicleListingIdentifier == null || vehicleListingCollectionIdentifiers.includes(vehicleListingIdentifier)) {
          return false;
        }
        vehicleListingCollectionIdentifiers.push(vehicleListingIdentifier);
        return true;
      });
      return [...vehicleListingsToAdd, ...vehicleListingCollection];
    }
    return vehicleListingCollection;
  }
}
