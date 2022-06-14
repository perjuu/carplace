import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ICarModel } from '../car-model.model';
import { CarModelService } from '../service/car-model.service';

@Component({
  templateUrl: './car-model-delete-dialog.component.html',
})
export class CarModelDeleteDialogComponent {
  carModel?: ICarModel;

  constructor(protected carModelService: CarModelService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.carModelService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
