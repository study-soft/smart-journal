import { Component, Input, OnInit } from '@angular/core';
import { Group } from 'app/shared/model/group.model';

@Component({
    selector: 'jhi-group-child',
    templateUrl: './group-child.component.html',
    styles: []
})
export class GroupChildComponent implements OnInit {
    @Input() group: Group;

    constructor() {}

    ngOnInit() {}
}
