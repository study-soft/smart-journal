import { Component, Input, OnInit } from '@angular/core';
import { Group } from 'app/shared/model/group.model';
import { Day } from 'app/shared/model/day.model';

const NUMBER_REGEX: RegExp = /^\d*\.?\d*$/g;

@Component({
    selector: 'jhi-board-journal',
    templateUrl: './board-journal.component.html',
    styles: []
})
export class BoardJournalComponent implements OnInit {
    @Input() group: Group;
    isEditing = false;
    formValid = true;

    constructor() {}

    ngOnInit() {}

    handleKeyup(event, day: Day) {
        const value = event.target.innerHTML;
        console.log('value string: ' + value);
        console.log('value number: ' + Number(value));
        if (value.match(NUMBER_REGEX)) {
            console.log('valid');
            this.formValid = true;
            event.target.classList.remove('alert-danger');
        } else {
            this.formValid = false;
            // event.target.className += ' alert-danger';
            event.target.classList.add('alert-danger');
        }
    }
}
