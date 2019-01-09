import { Moment } from 'moment';
import { Group } from 'app/shared/model//group.model';
import { Subject } from 'app/shared/model//subject.model';
import { DayType } from 'app/shared/model//day-type.model';

export class Board {
    constructor(
        public id?: number,
        public title?: string, // `${group.name}. ${subject.name}` - generated automatically
        public name?: string,
        public description?: string,
        public created?: Moment,
        public updated?: Moment,
        public group?: Group,
        public subject?: Subject,
        public dayTypes?: DayType[]
    ) {}
}
