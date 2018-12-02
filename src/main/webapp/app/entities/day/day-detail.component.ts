import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { Day } from 'app/shared/model/day.model';

@Component({
    selector: 'jhi-day-detail',
    templateUrl: './day-detail.component.html'
})
export class DayDetailComponent implements OnInit {
    day: Day;

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
