import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CarModelComponent } from '../list/car-model.component';
import { CarModelDetailComponent } from '../detail/car-model-detail.component';
import { CarModelUpdateComponent } from '../update/car-model-update.component';
import { CarModelRoutingResolveService } from './car-model-routing-resolve.service';

const carModelRoute: Routes = [
  {
    path: '',
    component: CarModelComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CarModelDetailComponent,
    resolve: {
      carModel: CarModelRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CarModelUpdateComponent,
    resolve: {
      carModel: CarModelRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CarModelUpdateComponent,
    resolve: {
      carModel: CarModelRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(carModelRoute)],
  exports: [RouterModule],
})
export class CarModelRoutingModule {}
