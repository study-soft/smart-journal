import { Moment } from 'moment';
import { IParty } from 'app/shared/model//party.model';
import { ISubject } from 'app/shared/model//subject.model';
import { IStudent } from 'app/shared/model//student.model';
import { IDayType } from 'app/shared/model//day-type.model';

export interface IBoard {
    id?: number;
    name?: string;
    description?: string;
    created?: Moment;
    updated?: Moment;
    party?: IParty;
    subject?: ISubject;
    students?: IStudent[];
    dayTypes?: IDayType[];
}

export class Board implements IBoard {
    constructor(
        public id?: number,
        public name?: string,
        public description?: string,
        public created?: Moment,
        public updated?: Moment,
        public party?: IParty,
        public subject?: ISubject,
        public students?: IStudent[],
        public dayTypes?: IDayType[]
    ) {}
}
