import { Component, Input, OnInit } from '@angular/core';
import { Student } from 'app/shared/model/student.model';
import { Day } from 'app/shared/model/day.model';
import { JhiEventManager } from 'ng-jhipster';

const NUMBER_REGEX: RegExp = /^\d*\.?\d*$/g;

@Component({
    selector: 'jhi-journal-update',
    templateUrl: './journal-update.component.html',
    styles: []
})
export class JournalUpdateComponent implements OnInit {
    @Input() students: Student[];
    formValid = true;
    invalidCount = 0;

    constructor(private eventManager: JhiEventManager) {}

    ngOnInit() {}

    handleKeyup(target: HTMLElement, day: Day) {
        const value = target.innerHTML;
        const isValid = value.match(NUMBER_REGEX);

        console.log('value string: ' + value);
        console.log('value number: ' + Number(value));

        // cell was valid but became invalid
        if (!isValid && target.dataset.valid === 'true') {
            if (this.invalidCount === 0) {
                this.formValid = false;
            }
            target.dataset.valid = 'false';
            this.invalidCount++;
            target.classList.add('alert-danger');
            // cell was invalid but became valid
        } else if (isValid && target.dataset.valid === 'false') {
            target.dataset.valid = 'true';
            this.invalidCount--;
            target.classList.remove('alert-danger');
            if (this.invalidCount === 0) {
                this.formValid = true;
            }
        }

        console.log(`invalid count = ${this.invalidCount}`);
        console.log('formValid = ', this.formValid);
    }

    disableEditingState() {
        this.eventManager.broadcast({
            name: 'changeJournalEditing',
            content: false
        });
    }
}
