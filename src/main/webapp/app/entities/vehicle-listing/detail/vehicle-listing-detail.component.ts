import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IVehicleListing } from '../vehicle-listing.model';

@Component({
  selector: 'jhi-vehicle-listing-detail',
  templateUrl: './vehicle-listing-detail.component.html',
})
export class VehicleListingDetailComponent implements OnInit {
  vehicleListing: IVehicleListing | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ vehicleListing }) => {
      this.vehicleListing = vehicleListing;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
