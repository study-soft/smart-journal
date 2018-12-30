import { Component, Input, OnDestroy, OnInit } from '@angular/core';
import { Group } from 'app/shared/model/group.model';
import { JhiEventManager } from 'ng-jhipster';
import { Subscription } from 'rxjs';

@Component({
    selector: 'jhi-board-journal',
    templateUrl: './board-journal.component.html',
    styles: []
})
export class BoardJournalComponent implements OnInit, OnDestroy {
    @Input() group: Group;
    isEditing = false;
    eventSubscriber: Subscription;

    constructor(private eventManager: JhiEventManager) {}

    ngOnInit() {
        this.registerChangeEditingState();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeEditingState() {
        this.eventSubscriber = this.eventManager.subscribe('changeJournalEditing', ({ content }) => {
            console.log('on changeJournalEditing: content = ', content);
            if (content !== null && content !== undefined && typeof content === 'boolean') {
                this.isEditing = content;
            }
        });
    }
}
