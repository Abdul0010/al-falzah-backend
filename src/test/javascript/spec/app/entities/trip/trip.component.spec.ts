import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { AlfalzahTestModule } from '../../../test.module';
import { TripComponent } from 'app/entities/trip/trip.component';
import { TripService } from 'app/entities/trip/trip.service';
import { Trip } from 'app/shared/model/trip.model';

describe('Component Tests', () => {
  describe('Trip Management Component', () => {
    let comp: TripComponent;
    let fixture: ComponentFixture<TripComponent>;
    let service: TripService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [AlfalzahTestModule],
        declarations: [TripComponent],
      })
        .overrideTemplate(TripComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(TripComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(TripService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new Trip(123)],
            headers,
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.trips && comp.trips[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
