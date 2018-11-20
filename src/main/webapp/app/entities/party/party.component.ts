import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { IParty } from 'app/shared/model/party.model';
import { Principal } from 'app/core';
import { PartyService } from './party.service';

@Component({
    selector: 'jhi-party',
    templateUrl: './party.component.html'
})
export class PartyComponent implements OnInit, OnDestroy {
    parties: IParty[];
    currentAccount: any;
    eventSubscriber: Subscription;

    constructor(
        private partyService: PartyService,
        private jhiAlertService: JhiAlertService,
        private eventManager: JhiEventManager,
        private principal: Principal
    ) {}

    loadAll() {
        this.partyService.query().subscribe(
            (res: HttpResponse<IParty[]>) => {
                this.parties = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    ngOnInit() {
        this.loadAll();
        this.principal.identity().then(account => {
            this.currentAccount = account;
        });
        this.registerChangeInParties();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: IParty) {
        return item.id;
    }

    registerChangeInParties() {
        this.eventSubscriber = this.eventManager.subscribe('partyListModification', response => this.loadAll());
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
