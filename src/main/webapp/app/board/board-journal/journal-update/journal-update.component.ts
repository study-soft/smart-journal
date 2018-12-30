import { Component, Input, OnInit } from '@angular/core';
import { Student } from 'app/shared/model/student.model';
import { Day } from 'app/shared/model/day.model';
import { JhiEventManager } from 'ng-jhipster';
import { BoardService } from 'app/board';
import { ActivatedRoute } from '@angular/router';

const NUMBER_REGEX: RegExp = /^\d*\.?\d*$/g;

@Component({
    selector: 'jhi-journal-update',
    templateUrl: './journal-update.component.html',
    styles: []
})
export class JournalUpdateComponent implements OnInit {
    @Input() students: Student[];
    boardId: number;
    formValid = true;
    invalidCount = 0;
    isUpdating = false;
    days: Day[] = []; // days for update
    lastResult: number; // last day result

    constructor(private eventManager: JhiEventManager, private boardService: BoardService, private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.boardId = this.activatedRoute.snapshot.params['id'];
        // Object.assign(this.students, this.students);
    }

    handleKeyup(target: HTMLElement) {
        const value = target.innerHTML;
        const isValid = value.match(NUMBER_REGEX);

        console.log('value string: ' + value);
        console.log('value number: ' + Number(value));

        if (isValid) {
            // day.result = Number(value);
            // console.log('day.result: ', day.result);
            // cell was invalid but became valid
            if (target.dataset.valid === 'false') {
                target.dataset.valid = 'true';
                this.invalidCount--;
                target.classList.remove('alert-danger');
                if (this.invalidCount === 0) {
                    this.formValid = true;
                }
            }
            // cell was valid but became invalid
        } else if (!isValid && target.dataset.valid === 'true') {
            if (this.invalidCount === 0) {
                this.formValid = false;
            }
            target.dataset.valid = 'false';
            this.invalidCount++;
            target.classList.add('alert-danger');
        }

        console.log(`invalid count = ${this.invalidCount}`);
        console.log('formValid = ', this.formValid);
    }

    handleFocus(target: HTMLElement) {
        this.lastResult = Number(target.innerHTML);
    }

    handleBlur(target: HTMLElement, day: Day) {
        const isValid = target.innerHTML.match(NUMBER_REGEX);
        const value = Number(target.innerHTML);

        if (this.lastResult !== value && isValid) {
            day = Object.assign({}, day);
            day.result = value;
            const index = this.days.findIndex(item => item.id === day.id);
            if (index === -1) {
                this.days.push(day);
            } else {
                this.days[index] = day;
            }
            day.result = Number(value);
            console.log('day.result: ', day.result);
        }
    }

    disableEditingState() {
        this.eventManager.broadcast({
            name: 'changeJournalEditing',
            content: false
        });
    }

    private updateDays() {
        this.isUpdating = true;
        console.log('days', this.days);
        this.boardService.updateDaysResults(this.boardId, this.days).subscribe(
            res => {
                this.onUpdateDaysSuccess();
            },
            res => this.onUpdateDaysError()
        );
    }

    private onUpdateDaysSuccess() {
        this.isUpdating = false;
        this.disableEditingState();
        this.eventManager.broadcast({
            name: 'daysListModification',
            content: 'Update days'
        });
    }

    private onUpdateDaysError() {
        this.isUpdating = false;
    }
}
