import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICarModel, getCarModelIdentifier } from '../car-model.model';

export type EntityResponseType = HttpResponse<ICarModel>;
export type EntityArrayResponseType = HttpResponse<ICarModel[]>;

@Injectable({ providedIn: 'root' })
export class CarModelService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/car-models');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(carModel: ICarModel): Observable<EntityResponseType> {
    return this.http.post<ICarModel>(this.resourceUrl, carModel, { observe: 'response' });
  }

  update(carModel: ICarModel): Observable<EntityResponseType> {
    return this.http.put<ICarModel>(`${this.resourceUrl}/${getCarModelIdentifier(carModel) as number}`, carModel, { observe: 'response' });
  }

  partialUpdate(carModel: ICarModel): Observable<EntityResponseType> {
    return this.http.patch<ICarModel>(`${this.resourceUrl}/${getCarModelIdentifier(carModel) as number}`, carModel, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ICarModel>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICarModel[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addCarModelToCollectionIfMissing(carModelCollection: ICarModel[], ...carModelsToCheck: (ICarModel | null | undefined)[]): ICarModel[] {
    const carModels: ICarModel[] = carModelsToCheck.filter(isPresent);
    if (carModels.length > 0) {
      const carModelCollectionIdentifiers = carModelCollection.map(carModelItem => getCarModelIdentifier(carModelItem)!);
      const carModelsToAdd = carModels.filter(carModelItem => {
        const carModelIdentifier = getCarModelIdentifier(carModelItem);
        if (carModelIdentifier == null || carModelCollectionIdentifiers.includes(carModelIdentifier)) {
          return false;
        }
        carModelCollectionIdentifiers.push(carModelIdentifier);
        return true;
      });
      return [...carModelsToAdd, ...carModelCollection];
    }
    return carModelCollection;
  }
}
