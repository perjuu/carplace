import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICarModel } from '../car-model.model';

@Component({
  selector: 'jhi-car-model-detail',
  templateUrl: './car-model-detail.component.html',
})
export class CarModelDetailComponent implements OnInit {
  carModel: ICarModel | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ carModel }) => {
      this.carModel = carModel;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
