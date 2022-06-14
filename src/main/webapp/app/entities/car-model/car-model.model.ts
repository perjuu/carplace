import { IVehicleListing } from 'app/entities/vehicle-listing/vehicle-listing.model';
import { ICategory } from 'app/entities/category/category.model';

export interface ICarModel {
  id?: number;
  make?: string | null;
  model?: string | null;
  launchYear?: number | null;
  vehicleListings?: IVehicleListing[] | null;
  categories?: ICategory[] | null;
}

export class CarModel implements ICarModel {
  constructor(
    public id?: number,
    public make?: string | null,
    public model?: string | null,
    public launchYear?: number | null,
    public vehicleListings?: IVehicleListing[] | null,
    public categories?: ICategory[] | null
  ) {}
}

export function getCarModelIdentifier(carModel: ICarModel): number | undefined {
  return carModel.id;
}
