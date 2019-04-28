import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT, DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { JhiAlertService } from 'ng-jhipster';

import { Board } from 'app/shared/model/board.model';
import { Subject } from 'app/shared/model/subject.model';
import { Group } from 'app/shared/model/group.model';
import { BoardService } from 'app/board/board.service';
import { SubjectService } from 'app/subject';
import { GroupService } from 'app/group/group.service';
import { Moment } from 'moment';
import { convertDateToString } from 'app/shared/util/date-util';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';

@Component({
    selector: 'jhi-board-update',
    templateUrl: './board-update.component.html'
})
export class BoardUpdateComponent implements OnInit {
    board: Board;
    isSaving = false;

    groups: Group[];

    subjects: Subject[];
    created: string;
    updated: string;

    dateFrom: Moment;
    dateTo: Moment;
    days: Set<number> = new Set();

    editForm: FormGroup;

    constructor(
        private jhiAlertService: JhiAlertService,
        private boardService: BoardService,
        private groupService: GroupService,
        private subjectService: SubjectService,
        private activatedRoute: ActivatedRoute,
        private fb: FormBuilder
    ) {}

    ngOnInit() {
        this.createForm();

        this.activatedRoute.data.subscribe(({ board }) => {
            this.board = board;
            this.created = this.board.created != null ? this.board.created.format(DATE_TIME_FORMAT) : null;
            this.updated = this.board.updated != null ? this.board.updated.format(DATE_TIME_FORMAT) : null;

            this.editForm.patchValue({
                id: this.board.id,
                title: this.board.title,
                name: this.board.name,
                description: this.board.description,
                group: this.board.group,
                subject: this.board.subject,
                days: {
                    dateFrom: null,
                    dateTo: null
                }
            });
        });
        this.groupService.query({ filter: 'board-is-null' }).subscribe(
            (res: HttpResponse<Group[]>) => {
                if (!this.board.group || !this.board.group.id) {
                    this.groups = res.body;
                } else {
                    this.groupService.find(this.board.group.id).subscribe(
                        (subRes: HttpResponse<Group>) => {
                            this.groups = [subRes.body].concat(res.body);
                        },
                        (subRes: HttpErrorResponse) => this.onError(subRes.message)
                    );
                }
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        this.subjectService.query({ filter: 'board-is-null' }).subscribe(
            (res: HttpResponse<Subject[]>) => {
                if (!this.board.subject || !this.board.subject.id) {
                    this.subjects = res.body;
                } else {
                    this.subjectService.find(this.board.subject.id).subscribe(
                        (subRes: HttpResponse<Subject>) => {
                            this.subjects = [subRes.body].concat(res.body);
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

    createForm() {
        this.editForm = this.fb.group({
            id: [undefined],
            title: [undefined],
            name: [undefined, Validators.required],
            description: [undefined],
            group: [undefined, Validators.required],
            subject: [undefined, Validators.required],
            days: this.fb.group({
                dateFrom: [undefined],
                dateTo: [undefined]
            })
        });
    }

    updateDayOfWeek(target: HTMLInputElement) {
        const dayNumber = +target.name;
        if (target.checked) {
            this.days.add(dayNumber);
        } else {
            this.days.delete(dayNumber);
        }

        console.log('days: ', this.days);
        console.log('days string: ' + JSON.stringify(Array.from(this.days).join(',')));
    }

    save() {
        this.board.title = `${this.board.group.name}. ${this.board.subject.name}`;
        this.isSaving = true;
        this.board.created = this.created != null ? moment(this.created, DATE_TIME_FORMAT) : null;
        this.board.updated = this.updated != null ? moment(this.updated, DATE_TIME_FORMAT) : null;
        if (this.board.id !== undefined) {
            this.subscribeToSaveResponse(this.boardService.update(this.board));
        } else {
            const req: any = {};
            if (this.dateFrom) {
                req.dateFrom = convertDateToString(this.dateFrom);
            }
            if (this.dateTo) {
                req.dateTo = convertDateToString(this.dateTo);
            }
            if (this.days.size !== 0) {
                req.days = Array.from(this.days).join(',');
            }
            this.subscribeToSaveResponse(this.boardService.create(this.board, req));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<Board>>) {
        result.subscribe((res: HttpResponse<Board>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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

    trackGroupById(index: number, item: Group) {
        return item.id;
    }

    trackSubjectById(index: number, item: Subject) {
        return item.id;
    }
}
