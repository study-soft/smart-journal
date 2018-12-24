import { Component, Input, OnInit } from '@angular/core';
import { Group } from 'app/shared/model/group.model';

@Component({
    selector: 'jhi-board-journal',
    templateUrl: './board-journal.component.html',
    styles: []
})
export class BoardJournalComponent implements OnInit {
    @Input() group: Group;

    constructor() {}

    ngOnInit() {}
}
