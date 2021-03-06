import { Day } from 'app/shared/model//day.model';
import { Board } from 'app/shared/model//board.model';

export class Student {
    constructor(
        public id?: number,
        public firstName?: string,
        public lastName?: string,
        public middleName?: string,
        public days?: Day[],
        public board?: Board
    ) {}
}
