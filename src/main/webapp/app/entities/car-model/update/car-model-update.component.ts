import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ICarModel, CarModel } from '../car-model.model';
import { CarModelService } from '../service/car-model.service';
import { ICategory } from 'app/entities/category/category.model';
import { CategoryService } from 'app/entities/category/service/category.service';

@Component({
  selector: 'jhi-car-model-update',
  templateUrl: './car-model-update.component.html',
})
export class CarModelUpdateComponent implements OnInit {
  isSaving = false;

  categoriesSharedCollection: ICategory[] = [];

  editForm = this.fb.group({
    id: [],
    make: [],
    model: [],
    launchYear: [],
    categories: [],
  });

  constructor(
    protected carModelService: CarModelService,
    protected categoryService: CategoryService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ carModel }) => {
      this.updateForm(carModel);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const carModel = this.createFromForm();
    if (carModel.id !== undefined) {
      this.subscribeToSaveResponse(this.carModelService.update(carModel));
    } else {
      this.subscribeToSaveResponse(this.carModelService.create(carModel));
    }
  }

  trackCategoryById(_index: number, item: ICategory): number {
    return item.id!;
  }

  getSelectedCategory(option: ICategory, selectedVals?: ICategory[]): ICategory {
    if (selectedVals) {
      for (const selectedVal of selectedVals) {
        if (option.id === selectedVal.id) {
          return selectedVal;
        }
      }
    }
    return option;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICarModel>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(carModel: ICarModel): void {
    this.editForm.patchValue({
      id: carModel.id,
      make: carModel.make,
      model: carModel.model,
      launchYear: carModel.launchYear,
      categories: carModel.categories,
    });

    this.categoriesSharedCollection = this.categoryService.addCategoryToCollectionIfMissing(
      this.categoriesSharedCollection,
      ...(carModel.categories ?? [])
    );
  }

  protected loadRelationshipsOptions(): void {
    this.categoryService
      .query()
      .pipe(map((res: HttpResponse<ICategory[]>) => res.body ?? []))
      .pipe(
        map((categories: ICategory[]) =>
          this.categoryService.addCategoryToCollectionIfMissing(categories, ...(this.editForm.get('categories')!.value ?? []))
        )
      )
      .subscribe((categories: ICategory[]) => (this.categoriesSharedCollection = categories));
  }

  protected createFromForm(): ICarModel {
    return {
      ...new CarModel(),
      id: this.editForm.get(['id'])!.value,
      make: this.editForm.get(['make'])!.value,
      model: this.editForm.get(['model'])!.value,
      launchYear: this.editForm.get(['launchYear'])!.value,
      categories: this.editForm.get(['categories'])!.value,
    };
  }
}
