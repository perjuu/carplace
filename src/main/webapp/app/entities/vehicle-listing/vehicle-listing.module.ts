import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { VehicleListingComponent } from './list/vehicle-listing.component';
import { VehicleListingDetailComponent } from './detail/vehicle-listing-detail.component';
import { VehicleListingUpdateComponent } from './update/vehicle-listing-update.component';
import { VehicleListingDeleteDialogComponent } from './delete/vehicle-listing-delete-dialog.component';
import { VehicleListingRoutingModule } from './route/vehicle-listing-routing.module';

@NgModule({
  imports: [SharedModule, VehicleListingRoutingModule],
  declarations: [
    VehicleListingComponent,
    VehicleListingDetailComponent,
    VehicleListingUpdateComponent,
    VehicleListingDeleteDialogComponent,
  ],
  entryComponents: [VehicleListingDeleteDialogComponent],
})
export class VehicleListingModule {}
