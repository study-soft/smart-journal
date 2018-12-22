import { Component, Input, OnInit } from '@angular/core';
import { Group } from 'app/shared/model/group.model';

@Component({
    selector: 'jhi-group-information',
    templateUrl: './group-information.component.html',
    styles: []
})
export class GroupInformationComponent implements OnInit {
    @Input() group: Group;

    constructor() {}

    ngOnInit() {}
}
