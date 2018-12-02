import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { Party } from 'app/shared/model/party.model';

type EntityResponseType = HttpResponse<Party>;
type EntityArrayResponseType = HttpResponse<Party[]>;

@Injectable({ providedIn: 'root' })
export class PartyService {
    public resourceUrl = SERVER_API_URL + 'api/parties';

    constructor(private http: HttpClient) {}

    create(party: Party): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(party);
        return this.http
            .post<Party>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(party: Party): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(party);
        return this.http
            .put<Party>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<Party>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<Party[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    protected convertDateFromClient(party: Party): Party {
        const copy: Party = Object.assign({}, party, {
            created: party.created != null && party.created.isValid() ? party.created.toJSON() : null,
            updated: party.updated != null && party.updated.isValid() ? party.updated.toJSON() : null
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
            res.body.forEach((party: Party) => {
                party.created = party.created != null ? moment(party.created) : null;
                party.updated = party.updated != null ? moment(party.updated) : null;
            });
        }
        return res;
    }
}
