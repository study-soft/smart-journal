import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { Day } from 'app/shared/model/day.model';

type EntityResponseType = HttpResponse<Day>;
type EntityArrayResponseType = HttpResponse<Day[]>;

@Injectable({ providedIn: 'root' })
export class DayService {
    public resourceUrl = SERVER_API_URL + 'api/days';

    constructor(private http: HttpClient) {}

    create(day: Day): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(day);
        return this.http
            .post<Day>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(day: Day): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(day);
        return this.http
            .put<Day>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<Day>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<Day[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    protected convertDateFromClient(day: Day): Day {
        const copy: Day = Object.assign({}, day, {
            date: day.date != null && day.date.isValid() ? day.date.toJSON() : null
        });
        return copy;
    }

    protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
        if (res.body) {
            res.body.date = res.body.date != null ? moment(res.body.date) : null;
        }
        return res;
    }

    protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        if (res.body) {
            res.body.forEach((day: Day) => {
                day.date = day.date != null ? moment(day.date) : null;
            });
        }
        return res;
    }
}
