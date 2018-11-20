import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { ISubject } from 'app/shared/model/subject.model';

type EntityResponseType = HttpResponse<ISubject>;
type EntityArrayResponseType = HttpResponse<ISubject[]>;

@Injectable({ providedIn: 'root' })
export class SubjectService {
    public resourceUrl = SERVER_API_URL + 'api/subjects';

    constructor(private http: HttpClient) {}

    create(subject: ISubject): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(subject);
        return this.http
            .post<ISubject>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(subject: ISubject): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(subject);
        return this.http
            .put<ISubject>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<ISubject>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<ISubject[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    protected convertDateFromClient(subject: ISubject): ISubject {
        const copy: ISubject = Object.assign({}, subject, {
            created: subject.created != null && subject.created.isValid() ? subject.created.toJSON() : null,
            updated: subject.updated != null && subject.updated.isValid() ? subject.updated.toJSON() : null
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
            res.body.forEach((subject: ISubject) => {
                subject.created = subject.created != null ? moment(subject.created) : null;
                subject.updated = subject.updated != null ? moment(subject.updated) : null;
            });
        }
        return res;
    }
}
