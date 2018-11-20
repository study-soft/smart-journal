import { Moment } from 'moment';
import { IStudent } from 'app/shared/model//student.model';
import { IDayType } from 'app/shared/model//day-type.model';

export interface IDay {
    id?: number;
    date?: Moment;
    result?: number;
    student?: IStudent;
    dayType?: IDayType;
}

export class Day implements IDay {
    constructor(public id?: number, public date?: Moment, public result?: number, public student?: IStudent, public dayType?: IDayType) {}
}
