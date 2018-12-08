import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { JhiAlertService } from 'ng-jhipster';

import { Subject } from 'app/shared/model/subject.model';
import { SubjectService } from 'app/subject';
import { Group } from 'app/shared/model/group.model';
import { GroupService } from 'app/group/group.service';

@Component({
    selector: 'jhi-group-update',
    templateUrl: './group-update.component.html'
})
export class GroupUpdateComponent implements OnInit {
    group: Group;
    isSaving: boolean;

    subjects: Subject[];
    created: string;
    updated: string;

    constructor(
        private jhiAlertService: JhiAlertService,
        private groupService: GroupService,
        private subjectService: SubjectService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ group: group }) => {
            this.group = group;
            this.created = this.group.created != null ? this.group.created.format(DATE_TIME_FORMAT) : null;
            this.updated = this.group.updated != null ? this.group.updated.format(DATE_TIME_FORMAT) : null;
        });
        this.subjectService.query().subscribe(
            (res: HttpResponse<Subject[]>) => {
                this.subjects = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        this.group.created = this.created != null ? moment(this.created, DATE_TIME_FORMAT) : null;
        this.group.updated = this.updated != null ? moment(this.updated, DATE_TIME_FORMAT) : null;
        if (this.group.id !== undefined) {
            this.subscribeToSaveResponse(this.groupService.update(this.group));
        } else {
            this.subscribeToSaveResponse(this.groupService.create(this.group));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<Group>>) {
        result.subscribe((res: HttpResponse<Group>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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

    trackSubjectById(index: number, item: Subject) {
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
