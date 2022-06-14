import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { VehicleListingService } from '../service/vehicle-listing.service';
import { IVehicleListing, VehicleListing } from '../vehicle-listing.model';
import { ICarModel } from 'app/entities/car-model/car-model.model';
import { CarModelService } from 'app/entities/car-model/service/car-model.service';

import { VehicleListingUpdateComponent } from './vehicle-listing-update.component';

describe('VehicleListing Management Update Component', () => {
  let comp: VehicleListingUpdateComponent;
  let fixture: ComponentFixture<VehicleListingUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let vehicleListingService: VehicleListingService;
  let carModelService: CarModelService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [VehicleListingUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(VehicleListingUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(VehicleListingUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    vehicleListingService = TestBed.inject(VehicleListingService);
    carModelService = TestBed.inject(CarModelService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call CarModel query and add missing value', () => {
      const vehicleListing: IVehicleListing = { id: 456 };
      const carModel: ICarModel = { id: 38531 };
      vehicleListing.carModel = carModel;

      const carModelCollection: ICarModel[] = [{ id: 75100 }];
      jest.spyOn(carModelService, 'query').mockReturnValue(of(new HttpResponse({ body: carModelCollection })));
      const additionalCarModels = [carModel];
      const expectedCollection: ICarModel[] = [...additionalCarModels, ...carModelCollection];
      jest.spyOn(carModelService, 'addCarModelToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ vehicleListing });
      comp.ngOnInit();

      expect(carModelService.query).toHaveBeenCalled();
      expect(carModelService.addCarModelToCollectionIfMissing).toHaveBeenCalledWith(carModelCollection, ...additionalCarModels);
      expect(comp.carModelsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const vehicleListing: IVehicleListing = { id: 456 };
      const carModel: ICarModel = { id: 96573 };
      vehicleListing.carModel = carModel;

      activatedRoute.data = of({ vehicleListing });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(vehicleListing));
      expect(comp.carModelsSharedCollection).toContain(carModel);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<VehicleListing>>();
      const vehicleListing = { id: 123 };
      jest.spyOn(vehicleListingService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ vehicleListing });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: vehicleListing }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(vehicleListingService.update).toHaveBeenCalledWith(vehicleListing);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<VehicleListing>>();
      const vehicleListing = new VehicleListing();
      jest.spyOn(vehicleListingService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ vehicleListing });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: vehicleListing }));
      saveSubject.complete();

      // THEN
      expect(vehicleListingService.create).toHaveBeenCalledWith(vehicleListing);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<VehicleListing>>();
      const vehicleListing = { id: 123 };
      jest.spyOn(vehicleListingService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ vehicleListing });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(vehicleListingService.update).toHaveBeenCalledWith(vehicleListing);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackCarModelById', () => {
      it('Should return tracked CarModel primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackCarModelById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
