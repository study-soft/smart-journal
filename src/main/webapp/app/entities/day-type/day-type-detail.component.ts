import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IDayType } from 'app/shared/model/day-type.model';

@Component({
    selector: 'jhi-day-type-detail',
    templateUrl: './day-type-detail.component.html'
})
export class DayTypeDetailComponent implements OnInit {
    dayType: IDayType;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ dayType }) => {
            this.dayType = dayType;
        });
    }

    previousState() {
        window.history.back();
    }
}
