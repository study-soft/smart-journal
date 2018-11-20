import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IDay } from 'app/shared/model/day.model';

@Component({
    selector: 'jhi-day-detail',
    templateUrl: './day-detail.component.html'
})
export class DayDetailComponent implements OnInit {
    day: IDay;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ day }) => {
            this.day = day;
        });
    }

    previousState() {
        window.history.back();
    }
}
