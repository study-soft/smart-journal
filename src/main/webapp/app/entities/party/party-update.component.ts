import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { JhiAlertService } from 'ng-jhipster';

import { Party } from 'app/shared/model/party.model';
import { PartyService } from './party.service';
import { Subject } from 'app/shared/model/subject.model';
import { SubjectService } from 'app/entities/subject';

@Component({
    selector: 'jhi-party-update',
    templateUrl: './party-update.component.html'
})
export class PartyUpdateComponent implements OnInit {
    party: Party;
    isSaving: boolean;

    subjects: Subject[];
    created: string;
    updated: string;

    constructor(
        private jhiAlertService: JhiAlertService,
        private partyService: PartyService,
        private subjectService: SubjectService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ party }) => {
            this.party = party;
            this.created = this.party.created != null ? this.party.created.format(DATE_TIME_FORMAT) : null;
            this.updated = this.party.updated != null ? this.party.updated.format(DATE_TIME_FORMAT) : null;
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
        this.party.created = this.created != null ? moment(this.created, DATE_TIME_FORMAT) : null;
        this.party.updated = this.updated != null ? moment(this.updated, DATE_TIME_FORMAT) : null;
        if (this.party.id !== undefined) {
            this.subscribeToSaveResponse(this.partyService.update(this.party));
        } else {
            this.subscribeToSaveResponse(this.partyService.create(this.party));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<Party>>) {
        result.subscribe((res: HttpResponse<Party>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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
