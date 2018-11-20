import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IDayType } from 'app/shared/model/day-type.model';

type EntityResponseType = HttpResponse<IDayType>;
type EntityArrayResponseType = HttpResponse<IDayType[]>;

@Injectable({ providedIn: 'root' })
export class DayTypeService {
    public resourceUrl = SERVER_API_URL + 'api/day-types';

    constructor(private http: HttpClient) {}

    create(dayType: IDayType): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(dayType);
        return this.http
            .post<IDayType>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(dayType: IDayType): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(dayType);
        return this.http
            .put<IDayType>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<IDayType>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IDayType[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    protected convertDateFromClient(dayType: IDayType): IDayType {
        const copy: IDayType = Object.assign({}, dayType, {
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
            res.body.forEach((dayType: IDayType) => {
                dayType.created = dayType.created != null ? moment(dayType.created) : null;
                dayType.updated = dayType.updated != null ? moment(dayType.updated) : null;
            });
        }
        return res;
    }
}
