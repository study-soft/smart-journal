import { Moment } from 'moment';
import { IParty } from 'app/shared/model//party.model';

export interface ISubject {
    id?: number;
    name?: string;
    description?: string;
    created?: Moment;
    updated?: Moment;
    groups?: IParty[];
}

export class Subject implements ISubject {
    constructor(
        public id?: number,
        public name?: string,
        public description?: string,
        public created?: Moment,
        public updated?: Moment,
        public groups?: IParty[]
    ) {}
}
