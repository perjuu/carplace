import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IVehicleListing } from '../vehicle-listing.model';
import { VehicleListingService } from '../service/vehicle-listing.service';

@Component({
  templateUrl: './vehicle-listing-delete-dialog.component.html',
})
export class VehicleListingDeleteDialogComponent {
  vehicleListing?: IVehicleListing;

  constructor(protected vehicleListingService: VehicleListingService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.vehicleListingService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
