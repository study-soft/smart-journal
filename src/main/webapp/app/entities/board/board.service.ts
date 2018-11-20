import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IBoard } from 'app/shared/model/board.model';

type EntityResponseType = HttpResponse<IBoard>;
type EntityArrayResponseType = HttpResponse<IBoard[]>;

@Injectable({ providedIn: 'root' })
export class BoardService {
    public resourceUrl = SERVER_API_URL + 'api/boards';

    constructor(private http: HttpClient) {}

    create(board: IBoard): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(board);
        return this.http
            .post<IBoard>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(board: IBoard): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(board);
        return this.http
            .put<IBoard>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<IBoard>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IBoard[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    protected convertDateFromClient(board: IBoard): IBoard {
        const copy: IBoard = Object.assign({}, board, {
            created: board.created != null && board.created.isValid() ? board.created.toJSON() : null,
            updated: board.updated != null && board.updated.isValid() ? board.updated.toJSON() : null
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
            res.body.forEach((board: IBoard) => {
                board.created = board.created != null ? moment(board.created) : null;
                board.updated = board.updated != null ? moment(board.updated) : null;
            });
        }
        return res;
    }
}
