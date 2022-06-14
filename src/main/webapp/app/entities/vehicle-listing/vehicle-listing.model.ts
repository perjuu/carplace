import { ICarModel } from 'app/entities/car-model/car-model.model';
import { FuelType } from 'app/entities/enumerations/fuel-type.model';
import { ListingStatus } from 'app/entities/enumerations/listing-status.model';

export interface IVehicleListing {
  id?: number;
  price?: number | null;
  year?: number | null;
  mileage?: number | null;
  fuel?: FuelType | null;
  status?: ListingStatus | null;
  carModel?: ICarModel | null;
}

export class VehicleListing implements IVehicleListing {
  constructor(
    public id?: number,
    public price?: number | null,
    public year?: number | null,
    public mileage?: number | null,
    public fuel?: FuelType | null,
    public status?: ListingStatus | null,
    public carModel?: ICarModel | null
  ) {}
}

export function getVehicleListingIdentifier(vehicleListing: IVehicleListing): number | undefined {
  return vehicleListing.id;
}
