import { Component, OnInit, OnDestroy, Input } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { DayType } from 'app/shared/model/day-type.model';
import { Principal } from 'app/core';
import { DayTypeService } from './day-type.service';
import { ActivatedRoute } from '@angular/router';

@Component({
    selector: 'jhi-day-type',
    templateUrl: './day-type.component.html'
})
export class DayTypeComponent implements OnInit, OnDestroy {
    @Input() dayTypes: DayType[];
    boardId: number;
    currentAccount: any;
    eventSubscriber: Subscription;

    constructor(
        private dayTypeService: DayTypeService,
        private jhiAlertService: JhiAlertService,
        private eventManager: JhiEventManager,
        private principal: Principal,
        private activatedRoute: ActivatedRoute
    ) {}

    loadAll() {
        this.dayTypeService.query().subscribe(
            (res: HttpResponse<DayType[]>) => {
                this.dayTypes = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    ngOnInit() {
        this.boardId = this.activatedRoute.snapshot.params['id'];
        this.principal.identity().then(account => {
            this.currentAccount = account;
        });
        this.registerChangeInDayTypes();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: DayType) {
        return item.id;
    }

    registerChangeInDayTypes() {
        this.eventSubscriber = this.eventManager.subscribe('dayTypeListModification', response => this.loadAll());
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
