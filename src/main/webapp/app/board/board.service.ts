import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { Moment } from 'moment';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption, DATE_FORMAT } from 'app/shared';
import { Board } from 'app/shared/model/board.model';
import { Day } from 'app/shared/model/day.model';
import { DayType } from 'app/shared/model/day-type.model';
import { log } from 'app/shared/decorator/log';

type BoardResponseType = HttpResponse<Board>;
type BoardArrayResponseType = HttpResponse<Board[]>;
type DayResponseType = HttpResponse<Day>;
type DayArrayResponseType = HttpResponse<Day[]>;
type DayTypeResponseType = HttpResponse<DayType>;
type DayTypeArrayResponseType = HttpResponse<DayType[]>;

// TODO: remove this type and do JS style
type requestOptions = {
    headers?: HttpHeaders | {
        [header: string]: string | string[];
    };
    observe: 'response';
    params?: HttpParams | {
        [param: string]: string | string[];
    };
    reportProgress?: boolean;
    responseType?: 'json';
    withCredentials?: boolean;
};

@Injectable({ providedIn: 'root' })
export class BoardService {
    public resourceUrl = SERVER_API_URL + 'api/boards';

    constructor(private http: HttpClient) {}

    create(board: Board, from: Moment, to: Moment, days: Set<number>): Observable<BoardResponseType> {
        const copy = this.convertDateFromClient(board);

        const daysString = Array.from(days).join(',');
        console.log(daysString);

        const options: requestOptions = {
            observe: 'response'
        };
        if (from && to && days) {
            options.params = new HttpParams()
                .set('from', this.convertDateToString(from))
                .set('to', this.convertDateToString(to))
                .set('days', daysString);
        }

        console.log(`query params: ${options.params ? options.params.toString() : 'null'}`);

        console.log(`perform POST ${this.resourceUrl}\n query: ${options.params ? options.params.toString() : 'null'}\n body: ${JSON.stringify(board)}`);

        return this.http
            .post<Board>(this.resourceUrl, copy, options)
            .pipe(map((res: BoardResponseType) => this.convertDateFromServer(res)));
    }

    update(board: Board): Observable<BoardResponseType> {
        const copy = this.convertDateFromClient(board);
        return this.http
            .put<Board>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: BoardResponseType) => this.convertDateFromServer(res)));
    }

    find(id: number): Observable<BoardResponseType> {
        return this.http
            .get<Board>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: BoardResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<BoardArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<Board[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: BoardArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    createDay(boardId: number, day: Day): Observable<DayResponseType> {
        const copy = this.convertDateFromClient(day);
        return this.http
            .post<Day>(`${this.resourceUrl}/${boardId}/days`, copy, { observe: 'response' })
            .pipe(map((res: DayResponseType) => this.convertDateFromServer(res)));
    }

    updateDay(boardId: number, day: Day): Observable<DayResponseType> {
        const copy = this.convertDateFromClient(day);
        return this.http
            .put<Day>(`${this.resourceUrl}/${boardId}/days`, copy, { observe: 'response' })
            .pipe(map((res: DayResponseType) => this.convertDateFromServer(res)));
    }

    updateDaysResults(boardId: number, days: Day[]): Observable<DayArrayResponseType> {
        // const copy = this.convertDateFromClient(days);
        console.log(`PUT ${this.resourceUrl}/${boardId}/days `, days);
        return this.http
            .patch<Day[]>(`${this.resourceUrl}/${boardId}/days`, days, { observe: 'response' });
    }

    findDay(boardId: number, dayId: number): Observable<DayResponseType> {
        return this.http
            .get<Day>(`${this.resourceUrl}/${boardId}/days/${dayId}`, { observe: 'response' })
            .pipe(map((res: DayResponseType) => this.convertDateFromServer(res)));
    }

    queryDays(boardId: number, req?: any): Observable<DayArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<Day[]>(`${this.resourceUrl}/${boardId}/days`, { params: options, observe: 'response' })
            .pipe(map((res: DayArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    deleteDay(boardId: number, dayId: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${boardId}/days/${dayId}`, { observe: 'response' });
    }

    createDayType(boardId: number, dayType: DayType): Observable<DayTypeResponseType> {
        const copy = this.convertDateFromClient(dayType);
        return this.http
            .post<DayType>(`${this.resourceUrl}/${boardId}/tasks`, copy, { observe: 'response' })
            .pipe(map((res: DayTypeResponseType) => this.convertDateFromServer(res)));
    }

    updateDayType(boardId: number, dayType: DayType): Observable<DayTypeResponseType> {
        const copy = this.convertDateFromClient(dayType);
        return this.http
            .put<DayType>(`${this.resourceUrl}/${boardId}/tasks`, copy, { observe: 'response' })
            .pipe(map((res: DayTypeResponseType) => this.convertDateFromServer(res)));
    }

    findDayType(boardId: number, dayTypeId: number): Observable<DayTypeResponseType> {
        return this.http
            .get<DayType>(`${this.resourceUrl}/${boardId}/tasks/${dayTypeId}`, { observe: 'response' })
            .pipe(map((res: DayTypeResponseType) => this.convertDateFromServer(res)));
    }

    queryDayType(boardId: number, req?: any): Observable<DayTypeArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<DayType[]>(`${this.resourceUrl}/${boardId}/tasks`, { params: options, observe: 'response' })
            .pipe(map((res: DayTypeArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    deleteDayType(boardId: number, dayTypeId: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${boardId}/tasks/${dayTypeId}`, { observe: 'response' });
    }

    protected convertDateFromClient(board: Board): Board {
        const copy: Board = Object.assign({}, board, {
            created: board.created != null && board.created.isValid() ? board.created.toJSON() : null,
            updated: board.updated != null && board.updated.isValid() ? board.updated.toJSON() : null
        });
        return copy;
    }

    protected convertDateToString(date: Moment): string {
        return date != null && date.isValid() ? date.format(DATE_FORMAT) : null;
    }

    protected convertDateFromServer(res: BoardResponseType): BoardResponseType {
        if (res.body) {
            res.body.created = res.body.created != null ? moment(res.body.created) : null;
            res.body.updated = res.body.updated != null ? moment(res.body.updated) : null;
        }
        return res;
    }

    protected convertDateArrayFromServer(res: BoardArrayResponseType): BoardArrayResponseType {
        if (res.body) {
            res.body.forEach((board: Board) => {
                board.created = board.created != null ? moment(board.created) : null;
                board.updated = board.updated != null ? moment(board.updated) : null;
            });
        }
        return res;
    }
}
