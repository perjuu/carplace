import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { VehicleListingDetailComponent } from './vehicle-listing-detail.component';

describe('VehicleListing Management Detail Component', () => {
  let comp: VehicleListingDetailComponent;
  let fixture: ComponentFixture<VehicleListingDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [VehicleListingDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ vehicleListing: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(VehicleListingDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(VehicleListingDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load vehicleListing on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.vehicleListing).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
