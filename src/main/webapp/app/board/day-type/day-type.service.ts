import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { DayType } from 'app/shared/model/day-type.model';

type EntityResponseType = HttpResponse<DayType>;
type EntityArrayResponseType = HttpResponse<DayType[]>;

@Injectable({ providedIn: 'root' })
export class DayTypeService {
    public resourceUrl = SERVER_API_URL + 'api/day-types';

    constructor(private http: HttpClient) {}

    create(dayType: DayType): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(dayType);
        return this.http
            .post<DayType>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(dayType: DayType): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(dayType);
        return this.http
            .put<DayType>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<DayType>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<DayType[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    protected convertDateFromClient(dayType: DayType): DayType {
        const copy: DayType = Object.assign({}, dayType, {
            created: dayType.created != null && dayType.created.isValid() ? dayType.created.toJSON() : null,
            updated: dayType.updated != null && dayType.updated.isValid() ? dayType.updated.toJSON() : null
        });
        return copy;
    }

    protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
        if (res.body) {
            res.body.created = res.body.created != null ? moment(res.body.created) : null;
            res.body.updated = res.body.updated != null ? moment(res.body.updated) : null;
        }
        return res;
    }

    protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        if (res.body) {
            res.body.forEach((dayType: DayType) => {
                dayType.created = dayType.created != null ? moment(dayType.created) : null;
                dayType.updated = dayType.updated != null ? moment(dayType.updated) : null;
            });
        }
        return res;
    }
}
