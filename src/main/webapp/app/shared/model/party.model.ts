import { Moment } from 'moment';
import { Subject } from 'app/shared/model//subject.model';

export class Party implements Party {
    constructor(
        public id?: number,
        public name?: string,
        public description?: string,
        public created?: Moment,
        public updated?: Moment,
        public subjects?: Subject[]
    ) {}
}
