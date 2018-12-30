import { Component, Input, OnInit } from '@angular/core';
import { Student } from 'app/shared/model/student.model';
import { JhiEventManager } from 'ng-jhipster';

@Component({
    selector: 'jhi-journal-detail',
    templateUrl: './journal-detail.component.html',
    styles: []
})
export class JournalDetailComponent implements OnInit {
    @Input() students: Student[];

    constructor(private eventManager: JhiEventManager) {}

    ngOnInit() {}

    enableEditingState() {
        this.eventManager.broadcast({
            name: 'changeJournalEditing',
            content: true
        });
    }
}
