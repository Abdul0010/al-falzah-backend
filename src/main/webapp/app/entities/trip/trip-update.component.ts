import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';

import { ITrip, Trip } from 'app/shared/model/trip.model';
import { TripService } from './trip.service';

@Component({
  selector: 'jhi-trip-update',
  templateUrl: './trip-update.component.html',
})
export class TripUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    source: [],
    destination: [],
    startTime: [],
    car: [],
    passengers: [],
    contactNo: [],
  });

  constructor(protected tripService: TripService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ trip }) => {
      if (!trip.id) {
        const today = moment().startOf('day');
        trip.startTime = today;
      }

      this.updateForm(trip);
    });
  }

  updateForm(trip: ITrip): void {
    this.editForm.patchValue({
      id: trip.id,
      source: trip.source,
      destination: trip.destination,
      startTime: trip.startTime ? trip.startTime.format(DATE_TIME_FORMAT) : null,
      car: trip.car,
      passengers: trip.passengers,
      contactNo: trip.contactNo,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const trip = this.createFromForm();
    if (trip.id !== undefined) {
      this.subscribeToSaveResponse(this.tripService.update(trip));
    } else {
      this.subscribeToSaveResponse(this.tripService.create(trip));
    }
  }

  private createFromForm(): ITrip {
    return {
      ...new Trip(),
      id: this.editForm.get(['id'])!.value,
      source: this.editForm.get(['source'])!.value,
      destination: this.editForm.get(['destination'])!.value,
      startTime: this.editForm.get(['startTime'])!.value ? moment(this.editForm.get(['startTime'])!.value, DATE_TIME_FORMAT) : undefined,
      car: this.editForm.get(['car'])!.value,
      passengers: this.editForm.get(['passengers'])!.value,
      contactNo: this.editForm.get(['contactNo'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITrip>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }
}
