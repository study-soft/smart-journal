import { Component, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Group } from 'app/shared/model/group.model';
import { NgbModal, NgbTabset } from '@ng-bootstrap/ng-bootstrap';
import { StudentUpdateDialogComponent } from 'app/group/student-update-dialog/student-update-dialog.component';
import { JhiAlertService, JhiEventManager } from 'ng-jhipster';
import { Subscription } from 'rxjs';
import { Principal } from 'app/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Student } from 'app/shared/model/student.model';
import { GroupService } from 'app/group/group.service';

@Component({
    selector: 'jhi-group-detail',
    templateUrl: './group-detail.component.html'
})
export class GroupDetailComponent implements OnInit, OnDestroy {
    group: Group;
    eventSubscriber: Subscription;
    @ViewChild('tabset')
    private tabset: NgbTabset;
    currentAccount: any;

    constructor(
        private activatedRoute: ActivatedRoute,
        private modalService: NgbModal,
        private eventManager: JhiEventManager,
        private principal: Principal,
        private groupService: GroupService,
        private jhiAlertService: JhiAlertService
    ) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ group: group }) => {
            this.group = group;
        });

        this.principal.identity().then(account => {
            this.currentAccount = account;
        });

        this.registerChangeInStudents();
        this.registerNavigationToStudentsTab();
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

    private loadStudents() {
        this.groupService.queryStudents(this.group.id).subscribe(
            (res: HttpResponse<Student[]>) => {
                console.log('loadStudents() students = ', res.body);
                this.group.students = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    private registerNavigationToStudentsTab() {
        this.eventManager.subscribe('navigationToStudentsTab', () => {
            this.tabset.select('students');
        });
    }

    private registerChangeInStudents() {
        this.eventSubscriber = this.eventManager.subscribe('groupStudentListModification',
            response => this.loadStudents());
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
