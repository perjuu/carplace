import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { VehicleListingComponent } from '../list/vehicle-listing.component';
import { VehicleListingDetailComponent } from '../detail/vehicle-listing-detail.component';
import { VehicleListingUpdateComponent } from '../update/vehicle-listing-update.component';
import { VehicleListingRoutingResolveService } from './vehicle-listing-routing-resolve.service';

const vehicleListingRoute: Routes = [
  {
    path: '',
    component: VehicleListingComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: VehicleListingDetailComponent,
    resolve: {
      vehicleListing: VehicleListingRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: VehicleListingUpdateComponent,
    resolve: {
      vehicleListing: VehicleListingRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: VehicleListingUpdateComponent,
    resolve: {
      vehicleListing: VehicleListingRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(vehicleListingRoute)],
  exports: [RouterModule],
})
export class VehicleListingRoutingModule {}
