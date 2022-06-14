import { ICarModel } from 'app/entities/car-model/car-model.model';

export interface ICategory {
  id?: number;
  name?: string | null;
  carModels?: ICarModel[] | null;
}

export class Category implements ICategory {
  constructor(public id?: number, public name?: string | null, public carModels?: ICarModel[] | null) {}
}

export function getCategoryIdentifier(category: ICategory): number | undefined {
  return category.id;
}
