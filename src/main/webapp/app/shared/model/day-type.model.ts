import { Moment } from 'moment';
import { IBoard } from 'app/shared/model//board.model';

export const enum Type {
    SIMPLE = 'SIMPLE',
    LAB = 'LAB',
    MODULE = 'MODULE',
    EXAM = 'EXAM',
    TEST = 'TEST'
}

export interface IDayType {
    id?: number;
    type?: Type;
    score?: number;
    description?: string;
    created?: Moment;
    updated?: Moment;
    board?: IBoard;
}

export class DayType implements IDayType {
    constructor(
        public id?: number,
        public type?: Type,
        public score?: number,
        public description?: string,
        public created?: Moment,
        public updated?: Moment,
        public board?: IBoard
    ) {}
}
