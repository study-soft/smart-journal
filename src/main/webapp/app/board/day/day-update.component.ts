import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { JhiAlertService } from 'ng-jhipster';

import { Day } from 'app/shared/model/day.model';
import { DayService } from './day.service';
import { Student } from 'app/shared/model/student.model';
import { StudentService } from 'app/group/student';
import { DayType } from 'app/shared/model/day-type.model';
import { DayTypeService } from 'app/board/day-type';

@Component({
    selector: 'jhi-day-update',
    templateUrl: './day-update.component.html'
})
export class DayUpdateComponent implements OnInit {
    day: Day;
    isSaving: boolean;

    students: Student[];

    daytypes: DayType[];
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
            (res: HttpResponse<Student[]>) => {
                this.students = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        this.dayTypeService.query({ filter: 'day-is-null' }).subscribe(
            (res: HttpResponse<DayType[]>) => {
                if (!this.day.dayType || !this.day.dayType.id) {
                    this.daytypes = res.body;
                } else {
                    this.dayTypeService.find(this.day.dayType.id).subscribe(
                        (subRes: HttpResponse<DayType>) => {
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

    private subscribeToSaveResponse(result: Observable<HttpResponse<Day>>) {
        result.subscribe((res: HttpResponse<Day>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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

    trackStudentById(index: number, item: Student) {
        return item.id;
    }

    trackDayTypeById(index: number, item: DayType) {
        return item.id;
    }
}
