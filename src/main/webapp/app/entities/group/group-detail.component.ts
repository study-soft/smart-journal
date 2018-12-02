import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Group } from 'app/shared/model/group.model';

@Component({
    selector: 'jhi-group-detail',
    templateUrl: './group-detail.component.html'
})
export class GroupDetailComponent implements OnInit {
    group: Group;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ group: group }) => {
            this.group = group;
        });
    }

    previousState() {
        window.history.back();
    }
}
