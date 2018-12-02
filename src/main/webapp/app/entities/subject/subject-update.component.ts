import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { JhiAlertService } from 'ng-jhipster';

import { Subject } from 'app/shared/model/subject.model';
import { SubjectService } from './subject.service';
import { GroupService } from 'app/entities/group';
import { Group } from 'app/shared/model/group.model';

@Component({
    selector: 'jhi-subject-update',
    templateUrl: './subject-update.component.html'
})
export class SubjectUpdateComponent implements OnInit {
    subject: Subject;
    isSaving: boolean;

    groups: Group[];
    created: string;
    updated: string;

    constructor(
        private jhiAlertService: JhiAlertService,
        private subjectService: SubjectService,
        private partyService: GroupService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ subject }) => {
            this.subject = subject;
            this.created = this.subject.created != null ? this.subject.created.format(DATE_TIME_FORMAT) : null;
            this.updated = this.subject.updated != null ? this.subject.updated.format(DATE_TIME_FORMAT) : null;
        });
        this.partyService.query().subscribe(
            (res: HttpResponse<Group[]>) => {
                this.groups = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        this.subject.created = this.created != null ? moment(this.created, DATE_TIME_FORMAT) : null;
        this.subject.updated = this.updated != null ? moment(this.updated, DATE_TIME_FORMAT) : null;
        if (this.subject.id !== undefined) {
            this.subscribeToSaveResponse(this.subjectService.update(this.subject));
        } else {
            this.subscribeToSaveResponse(this.subjectService.create(this.subject));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<Subject>>) {
        result.subscribe((res: HttpResponse<Subject>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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

    trackPartyById(index: number, item: Group) {
        return item.id;
    }

    getSelected(selectedVals: Array<any>, option: any) {
        if (selectedVals) {
            for (let i = 0; i < selectedVals.length; i++) {
                if (option.id === selectedVals[i].id) {
                    return selectedVals[i];
                }
            }
        }
        return option;
    }
}
