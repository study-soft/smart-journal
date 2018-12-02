import { Moment } from 'moment';
import { Group } from 'app/shared/model/group.model';

export class Subject {
    constructor(
        public id?: number,
        public name?: string,
        public description?: string,
        public created?: Moment,
        public updated?: Moment,
        public groups?: Group[]
    ) {}
}
