import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { Group } from 'app/shared/model/group.model';

type EntityResponseType = HttpResponse<Group>;
type EntityArrayResponseType = HttpResponse<Group[]>;

@Injectable({ providedIn: 'root' })
export class GroupService {
    public resourceUrl = SERVER_API_URL + 'api/groups';

    constructor(private http: HttpClient) {}

    create(group: Group): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(group);
        return this.http
            .post<Group>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(group: Group): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(group);
        return this.http
            .put<Group>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<Group>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<Group[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    protected convertDateFromClient(group: Group): Group {
        const copy: Group = Object.assign({}, group, {
            created: group.created != null && group.created.isValid() ? group.created.toJSON() : null,
            updated: group.updated != null && group.updated.isValid() ? group.updated.toJSON() : null
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
            res.body.forEach((group: Group) => {
                group.created = group.created != null ? moment(group.created) : null;
                group.updated = group.updated != null ? moment(group.updated) : null;
            });
        }
        return res;
    }
}
