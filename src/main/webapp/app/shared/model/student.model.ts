import { IDay } from 'app/shared/model//day.model';
import { IBoard } from 'app/shared/model//board.model';

export interface IStudent {
    id?: number;
    firstName?: string;
    lastName?: string;
    middleName?: string;
    rating?: number;
    days?: IDay[];
    board?: IBoard;
}

export class Student implements IStudent {
    constructor(
        public id?: number,
        public firstName?: string,
        public lastName?: string,
        public middleName?: string,
        public rating?: number,
        public days?: IDay[],
        public board?: IBoard
    ) {}
}
