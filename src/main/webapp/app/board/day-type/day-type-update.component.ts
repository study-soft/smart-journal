import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { JhiAlertService } from 'ng-jhipster';

import { DayType } from 'app/shared/model/day-type.model';
import { DayTypeService } from './day-type.service';
import { Board } from 'app/shared/model/board.model';
import { BoardService } from 'app/board/board.service';

@Component({
    selector: 'jhi-day-type-update',
    templateUrl: './day-type-update.component.html'
})
export class DayTypeUpdateComponent implements OnInit {
    dayType: DayType;
    isSaving: boolean;

    boards: Board[];
    created: string;
    updated: string;

    constructor(
        private jhiAlertService: JhiAlertService,
        private dayTypeService: DayTypeService,
        private boardService: BoardService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ dayType }) => {
            this.dayType = dayType;
            this.created = this.dayType.created != null ? this.dayType.created.format(DATE_TIME_FORMAT) : null;
            this.updated = this.dayType.updated != null ? this.dayType.updated.format(DATE_TIME_FORMAT) : null;
        });
        this.boardService.query().subscribe(
            (res: HttpResponse<Board[]>) => {
                this.boards = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        this.dayType.created = this.created != null ? moment(this.created, DATE_TIME_FORMAT) : null;
        this.dayType.updated = this.updated != null ? moment(this.updated, DATE_TIME_FORMAT) : null;
        if (this.dayType.id !== undefined) {
            this.subscribeToSaveResponse(this.dayTypeService.update(this.dayType));
        } else {
            this.subscribeToSaveResponse(this.dayTypeService.create(this.dayType));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<DayType>>) {
        result.subscribe((res: HttpResponse<DayType>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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

    trackBoardById(index: number, item: Board) {
        return item.id;
    }
}
