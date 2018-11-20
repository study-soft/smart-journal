import { Moment } from 'moment';
import { ISubject } from 'app/shared/model//subject.model';

export interface IParty {
    id?: number;
    name?: string;
    description?: string;
    created?: Moment;
    updated?: Moment;
    subjects?: ISubject[];
}

export class Party implements IParty {
    constructor(
        public id?: number,
        public name?: string,
        public description?: string,
        public created?: Moment,
        public updated?: Moment,
        public subjects?: ISubject[]
    ) {}
}
