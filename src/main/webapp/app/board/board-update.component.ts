import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { JhiAlertService } from 'ng-jhipster';

import { Board } from 'app/shared/model/board.model';
import { Subject } from 'app/shared/model/subject.model';
import { Group } from 'app/shared/model/group.model';
import { BoardService } from 'app/board/board.service';
import { SubjectService } from 'app/subject';
import { GroupService } from 'app/group/group.service';

@Component({
    selector: 'jhi-board-update',
    templateUrl: './board-update.component.html'
})
export class BoardUpdateComponent implements OnInit {
    board: Board;
    isSaving: boolean;

    groups: Group[];

    subjects: Subject[];
    created: string;
    updated: string;

    constructor(
        private jhiAlertService: JhiAlertService,
        private boardService: BoardService,
        private groupService: GroupService,
        private subjectService: SubjectService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ board }) => {
            this.board = board;
            this.created = this.board.created != null ? this.board.created.format(DATE_TIME_FORMAT) : null;
            this.updated = this.board.updated != null ? this.board.updated.format(DATE_TIME_FORMAT) : null;
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

    save() {
        this.isSaving = true;
        this.board.created = this.created != null ? moment(this.created, DATE_TIME_FORMAT) : null;
        this.board.updated = this.updated != null ? moment(this.updated, DATE_TIME_FORMAT) : null;
        if (this.board.id !== undefined) {
            this.subscribeToSaveResponse(this.boardService.update(this.board));
        } else {
            this.subscribeToSaveResponse(this.boardService.create(this.board));
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
