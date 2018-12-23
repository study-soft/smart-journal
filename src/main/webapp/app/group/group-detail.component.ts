import { Component, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Group } from 'app/shared/model/group.model';
import { NgbModal, NgbTabset } from '@ng-bootstrap/ng-bootstrap';
import { StudentUpdateDialogComponent } from 'app/group/student-update-dialog/student-update-dialog.component';
import { JhiEventManager } from 'ng-jhipster';
import { Subscription } from 'rxjs';

@Component({
    selector: 'jhi-group-detail',
    templateUrl: './group-detail.component.html'
})
export class GroupDetailComponent implements OnInit, OnDestroy {
    group: Group;
    eventSubscriber: Subscription;
    @ViewChild('tabset')
    private tabs: NgbTabset;

    constructor(private activatedRoute: ActivatedRoute, private modalService: NgbModal, private eventManager: JhiEventManager) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ group: group }) => {
            this.group = group;
        });

        this.navigateToStudentsTab();
    }

    ngOnDestroy() {
        if (this.eventSubscriber) {
            this.eventManager.destroy(this.eventSubscriber);
        }
    }

    previousState() {
        window.history.back();
    }

    addStudent() {
        const modalRef = this.modalService.open(StudentUpdateDialogComponent, { size: 'lg', backdrop: 'static' });
        console.log('this.group.id = ' + this.group.id);
        modalRef.componentInstance.groupId = this.group.id;
    }

    navigateToStudentsTab() {
        this.eventManager.subscribe('navigateToStudentsTab', () => {
            this.tabs.select('students');
        });
    }
}
