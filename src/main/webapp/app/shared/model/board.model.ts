import { Moment } from 'moment';
import { Group } from 'app/shared/model//group.model';
import { Subject } from 'app/shared/model//subject.model';
import { Student } from 'app/shared/model//student.model';
import { DayType } from 'app/shared/model//day-type.model';

export class Board {
    constructor(
        public id?: number,
        public title?: string,
        public name?: string,
        public description?: string,
        public created?: Moment,
        public updated?: Moment,
        public group?: Group,
        public subject?: Subject,
        public students?: Student[],
        public dayTypes?: DayType[]
    ) {}
}
