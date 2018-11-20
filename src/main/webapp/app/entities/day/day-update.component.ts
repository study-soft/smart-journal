import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { JhiAlertService } from 'ng-jhipster';

import { IDay } from 'app/shared/model/day.model';
import { DayService } from './day.service';
import { IStudent } from 'app/shared/model/student.model';
import { StudentService } from 'app/entities/student';
import { IDayType } from 'app/shared/model/day-type.model';
import { DayTypeService } from 'app/entities/day-type';

@Component({
    selector: 'jhi-day-update',
    templateUrl: './day-update.component.html'
})
export class DayUpdateComponent implements OnInit {
    day: IDay;
    isSaving: boolean;

    students: IStudent[];

    daytypes: IDayType[];
    date: string;

    constructor(
        private jhiAlertService: JhiAlertService,
        private dayService: DayService,
        private studentService: StudentService,
        private dayTypeService: DayTypeService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ day }) => {
            this.day = day;
            this.date = this.day.date != null ? this.day.date.format(DATE_TIME_FORMAT) : null;
        });
        this.studentService.query().subscribe(
            (res: HttpResponse<IStudent[]>) => {
                this.students = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        this.dayTypeService.query({ filter: 'day-is-null' }).subscribe(
            (res: HttpResponse<IDayType[]>) => {
                if (!this.day.dayType || !this.day.dayType.id) {
                    this.daytypes = res.body;
                } else {
                    this.dayTypeService.find(this.day.dayType.id).subscribe(
                        (subRes: HttpResponse<IDayType>) => {
                            this.daytypes = [subRes.body].concat(res.body);
                        },
                        (subRes: HttpErrorResponse) => this.onError(subRes.message)
                    );
                }
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        this.day.date = this.date != null ? moment(this.date, DATE_TIME_FORMAT) : null;
        if (this.day.id !== undefined) {
            this.subscribeToSaveResponse(this.dayService.update(this.day));
        } else {
            this.subscribeToSaveResponse(this.dayService.create(this.day));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IDay>>) {
        result.subscribe((res: HttpResponse<IDay>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    trackStudentById(index: number, item: IStudent) {
        return item.id;
    }

    trackDayTypeById(index: number, item: IDayType) {
        return item.id;
    }
}
