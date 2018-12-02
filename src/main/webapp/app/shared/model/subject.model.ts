import { Moment } from 'moment';
import { Party } from 'app/shared/model//party.model';

export class Subject implements Subject {
    constructor(
        public id?: number,
        public name?: string,
        public description?: string,
        public created?: Moment,
        public updated?: Moment,
        public groups?: Party[]
    ) {}
}
