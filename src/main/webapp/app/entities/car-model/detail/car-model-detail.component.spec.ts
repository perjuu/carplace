import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CarModelDetailComponent } from './car-model-detail.component';

describe('CarModel Management Detail Component', () => {
  let comp: CarModelDetailComponent;
  let fixture: ComponentFixture<CarModelDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [CarModelDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ carModel: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(CarModelDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(CarModelDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load carModel on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.carModel).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
