import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { DayType } from 'app/shared/model/day-type.model';

@Component({
    selector: 'jhi-day-type-detail',
    templateUrl: './day-type-detail.component.html'
})
export class DayTypeDetailComponent implements OnInit {
    dayType: DayType;

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
