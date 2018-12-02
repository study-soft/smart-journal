import { Day } from 'app/shared/model//day.model';
import { Board } from 'app/shared/model//board.model';

export class Student implements Student {
    constructor(
        public id?: number,
        public firstName?: string,
        public lastName?: string,
        public middleName?: string,
        public rating?: number,
        public days?: Day[],
        public board?: Board
    ) {}
}
