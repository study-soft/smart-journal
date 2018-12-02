import { Moment } from 'moment';
import { Party } from 'app/shared/model//party.model';
import { Subject } from 'app/shared/model//subject.model';
import { Student } from 'app/shared/model//student.model';
import { DayType } from 'app/shared/model//day-type.model';

export class Board {
    constructor(
        public id?: number,
        public name?: string,
        public description?: string,
        public created?: Moment,
        public updated?: Moment,
        public party?: Party,
        public subject?: Subject,
        public students?: Student[],
        public dayTypes?: DayType[]
    ) {}
}
