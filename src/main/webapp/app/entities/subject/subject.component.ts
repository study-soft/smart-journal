import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { ISubject } from 'app/shared/model/subject.model';
import { Principal } from 'app/core';
import { SubjectService } from './subject.service';

@Component({
    selector: 'jhi-subject',
    templateUrl: './subject.component.html'
})
export class SubjectComponent implements OnInit, OnDestroy {
    subjects: ISubject[];
    currentAccount: any;
    eventSubscriber: Subscription;

    constructor(
        private subjectService: SubjectService,
        private jhiAlertService: JhiAlertService,
        private eventManager: JhiEventManager,
        private principal: Principal
    ) {}

    loadAll() {
        this.subjectService.query().subscribe(
            (res: HttpResponse<ISubject[]>) => {
                this.subjects = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    ngOnInit() {
        this.loadAll();
        this.principal.identity().then(account => {
            this.currentAccount = account;
        });
        this.registerChangeInSubjects();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: ISubject) {
        return item.id;
    }

    registerChangeInSubjects() {
        this.eventSubscriber = this.eventManager.subscribe('subjectListModification', response => this.loadAll());
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
