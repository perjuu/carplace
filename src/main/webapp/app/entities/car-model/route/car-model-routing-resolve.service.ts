import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICarModel, CarModel } from '../car-model.model';
import { CarModelService } from '../service/car-model.service';

@Injectable({ providedIn: 'root' })
export class CarModelRoutingResolveService implements Resolve<ICarModel> {
  constructor(protected service: CarModelService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICarModel> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((carModel: HttpResponse<CarModel>) => {
          if (carModel.body) {
            return of(carModel.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new CarModel());
  }
}
