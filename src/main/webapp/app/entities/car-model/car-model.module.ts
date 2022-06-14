import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { CarModelComponent } from './list/car-model.component';
import { CarModelDetailComponent } from './detail/car-model-detail.component';
import { CarModelUpdateComponent } from './update/car-model-update.component';
import { CarModelDeleteDialogComponent } from './delete/car-model-delete-dialog.component';
import { CarModelRoutingModule } from './route/car-model-routing.module';

@NgModule({
  imports: [SharedModule, CarModelRoutingModule],
  declarations: [CarModelComponent, CarModelDetailComponent, CarModelUpdateComponent, CarModelDeleteDialogComponent],
  entryComponents: [CarModelDeleteDialogComponent],
})
export class CarModelModule {}
