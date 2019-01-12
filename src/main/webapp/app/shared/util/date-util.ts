import { isMoment, Moment } from 'moment';
import { DATE_FORMAT } from 'app/shared';

export const convertDateToString = (date: Moment, format = DATE_FORMAT): string => {
    return date != null && isMoment(date) && date.isValid() ? date.format(format) : null;
};
