import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'car-model',
        data: { pageTitle: 'carplaceApp.carModel.home.title' },
        loadChildren: () => import('./car-model/car-model.module').then(m => m.CarModelModule),
      },
      {
        path: 'category',
        data: { pageTitle: 'carplaceApp.category.home.title' },
        loadChildren: () => import('./category/category.module').then(m => m.CategoryModule),
      },
      {
        path: 'vehicle-listing',
        data: { pageTitle: 'carplaceApp.vehicleListing.home.title' },
        loadChildren: () => import('./vehicle-listing/vehicle-listing.module').then(m => m.VehicleListingModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
