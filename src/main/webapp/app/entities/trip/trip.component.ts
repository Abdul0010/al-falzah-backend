import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ITrip } from 'app/shared/model/trip.model';
import { TripService } from './trip.service';
import { TripDeleteDialogComponent } from './trip-delete-dialog.component';

@Component({
  selector: 'jhi-trip',
  templateUrl: './trip.component.html',
})
export class TripComponent implements OnInit, OnDestroy {
  trips?: ITrip[];
  eventSubscriber?: Subscription;

  constructor(protected tripService: TripService, protected eventManager: JhiEventManager, protected modalService: NgbModal) {}

  loadAll(): void {
    this.tripService.query().subscribe((res: HttpResponse<ITrip[]>) => (this.trips = res.body || []));
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInTrips();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: ITrip): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInTrips(): void {
    this.eventSubscriber = this.eventManager.subscribe('tripListModification', () => this.loadAll());
  }

  delete(trip: ITrip): void {
    const modalRef = this.modalService.open(TripDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.trip = trip;
  }
}
