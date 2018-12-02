import { Moment } from 'moment';
import { Board } from 'app/shared/model//board.model';

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
        public description?: string,
        public expiry?: number,
        public created?: Moment,
        public updated?: Moment,
        public board?: Board
    ) {}
}
