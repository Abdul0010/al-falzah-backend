import { Moment } from 'moment';

export interface ITrip {
  id?: number;
  source?: string;
  destination?: string;
  startTime?: Moment;
  car?: string;
  passengers?: number;
  contactNo?: string;
}

export class Trip implements ITrip {
  constructor(
    public id?: number,
    public source?: string,
    public destination?: string,
    public startTime?: Moment,
    public car?: string,
    public passengers?: number,
    public contactNo?: string
  ) {}
}
