import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { IDay } from 'app/shared/model/day.model';
import { Principal } from 'app/core';
import { DayService } from './day.service';

@Component({
    selector: 'jhi-day',
    templateUrl: './day.component.html'
})
export class DayComponent implements OnInit, OnDestroy {
    days: IDay[];
    currentAccount: any;
    eventSubscriber: Subscription;

    constructor(
        private dayService: DayService,
        private jhiAlertService: JhiAlertService,
        private eventManager: JhiEventManager,
        private principal: Principal
    ) {}

    loadAll() {
        this.dayService.query().subscribe(
            (res: HttpResponse<IDay[]>) => {
                this.days = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    ngOnInit() {
        this.loadAll();
        this.principal.identity().then(account => {
            this.currentAccount = account;
        });
        this.registerChangeInDays();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: IDay) {
        return item.id;
    }

    registerChangeInDays() {
        this.eventSubscriber = this.eventManager.subscribe('dayListModification', response => this.loadAll());
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
