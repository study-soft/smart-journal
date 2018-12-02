import { Moment } from 'moment';
import { Student } from 'app/shared/model//student.model';
import { DayType } from 'app/shared/model//day-type.model';

export class Day {
    constructor(
        public id?: number,
        public date?: Moment,
        public result?: number,
        public student?: Student,
        public dayType?: DayType
    ) {}
}
