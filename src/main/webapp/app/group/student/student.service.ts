import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { Student } from 'app/shared/model/student.model';

type EntityResponseType = HttpResponse<Student>;
type EntityArrayResponseType = HttpResponse<Student[]>;

@Injectable({ providedIn: 'root' })
export class StudentService {
    public resourceUrl = SERVER_API_URL + 'api/students';

    constructor(private http: HttpClient) {}

    create(student: Student): Observable<EntityResponseType> {
        return this.http.post<Student>(this.resourceUrl, student, { observe: 'response' });
    }

    update(student: Student): Observable<EntityResponseType> {
        return this.http.put<Student>(this.resourceUrl, student, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<Student>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<Student[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }
}
