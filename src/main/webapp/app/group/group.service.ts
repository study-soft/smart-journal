import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { Group } from 'app/shared/model/group.model';
import { Student } from 'app/shared/model/student.model';

type GroupResponseType = HttpResponse<Group>;
type GroupArrayResponseType = HttpResponse<Group[]>;
type StudentResponseType = HttpResponse<Student>;
type StudentArrayResponseType = HttpResponse<Student[]>;

@Injectable({ providedIn: 'root' })
export class GroupService {
    public resourceUrl = SERVER_API_URL + 'api/groups';

    constructor(private http: HttpClient) {}

    create(group: Group): Observable<GroupResponseType> {
        const copy = this.convertDateFromClient(group);
        return this.http
            .post<Group>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: GroupResponseType) => this.convertDateFromServer(res)));
    }

    update(group: Group): Observable<GroupResponseType> {
        const copy = this.convertDateFromClient(group);
        return this.http
            .put<Group>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: GroupResponseType) => this.convertDateFromServer(res)));
    }

    find(id: number): Observable<GroupResponseType> {
        return this.http
            .get<Group>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: GroupResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<GroupArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<Group[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: GroupArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    queryStudents(groupId: number, req?: any): Observable<StudentArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<Student[]>(`${this.resourceUrl}/${groupId}/students`,
                { params: options, observe: 'response'});
    }

    createStudent(groupId: number, student: Student): Observable<StudentResponseType> {
        return this.http.post<Student>(`${this.resourceUrl}/${groupId}/students`, student, { observe: 'response' });
    }

    updateStudent(groupId: number, student: Student): Observable<StudentResponseType> {
        console.log(`PUT /groups/${groupId}/students: student = `, student);
        return this.http.put<Student>(`${this.resourceUrl}/${groupId}/students`, student, { observe: 'response' });
    }

    deleteStudent(groupId: number, studentId: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${groupId}/students/${studentId}`, { observe: 'response' });
    }

    protected convertDateFromClient(group: Group): Group {
        const copy: Group = Object.assign({}, group, {
            created: group.created != null && group.created.isValid() ? group.created.toJSON() : null,
            updated: group.updated != null && group.updated.isValid() ? group.updated.toJSON() : null
        });
        return copy;
    }

    protected convertDateFromServer(res: GroupResponseType): GroupResponseType {
        if (res.body) {
            res.body.created = res.body.created != null ? moment(res.body.created) : null;
            res.body.updated = res.body.updated != null ? moment(res.body.updated) : null;
        }
        return res;
    }

    protected convertDateArrayFromServer(res: GroupArrayResponseType): GroupArrayResponseType {
        if (res.body) {
            res.body.forEach((group: Group) => {
                group.created = group.created != null ? moment(group.created) : null;
                group.updated = group.updated != null ? moment(group.updated) : null;
            });
        }
        return res;
    }
}
