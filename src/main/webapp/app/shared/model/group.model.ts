import { Moment } from 'moment';
import { Subject } from 'app/shared/model//subject.model';
import { Student } from 'app/shared/model/student.model';

export class Group {
    constructor(
        public id?: number,
        public name?: string,
        public description?: string,
        public created?: Moment,
        public updated?: Moment,
        public subjects?: Subject[],
        public students?: Student[]
    ) {}
}
