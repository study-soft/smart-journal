import { Moment } from 'moment';
import { Board } from 'app/shared/model//board.model';
import { Day } from 'app/shared/model/day.model';

export const enum Type {
    SIMPLE = 'SIMPLE',
    LAB = 'LAB',
    MODULE = 'MODULE',
    EXAM = 'EXAM',
    TEST = 'TEST'
}

export class DayType {
    constructor(
        public id?: number,
        public type?: Type,
        public score?: number,
        public name?: string,
        public description?: string,
        public expiry?: number,
        public created?: Moment,
        public updated?: Moment,
        public board?: Board,
        public days?: Day[]
    ) {}
}
