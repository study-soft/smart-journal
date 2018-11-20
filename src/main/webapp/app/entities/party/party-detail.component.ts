import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IParty } from 'app/shared/model/party.model';

@Component({
    selector: 'jhi-party-detail',
    templateUrl: './party-detail.component.html'
})
export class PartyDetailComponent implements OnInit {
    party: IParty;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ party }) => {
            this.party = party;
        });
    }

    previousState() {
        window.history.back();
    }
}
