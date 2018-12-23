import { Component, Input, OnDestroy, OnInit } from '@angular/core';
import { Student } from 'app/shared/model/student.model';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { StudentUpdateDialogComponent } from 'app/group/student-update-dialog/student-update-dialog.component';
import { StudentDeleteDialogComponent } from 'app/group/student-delete-dialog/student-delete-dialog.component';
import { Subscription } from 'rxjs';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { JhiAlertService, JhiEventManager } from 'ng-jhipster';
import { Principal } from 'app/core';
import { GroupService } from 'app/group';

@Component({
    selector: 'jhi-group-students',
    templateUrl: './group-students.component.html',
    styles: []
})
export class GroupStudentsComponent implements OnInit, OnDestroy {
    @Input() groupId: number;
    students: Student[];
    currentAccount: any;
    eventSubscriber: Subscription;

    constructor(
        private modalService: NgbModal,
        private groupService: GroupService,
        private jhiAlertService: JhiAlertService,
        private eventManager: JhiEventManager,
        private principal: Principal
    ) {}

    loadAll() {
        this.groupService.queryStudents(this.groupId).subscribe(
            (res: HttpResponse<Student[]>) => {
                this.students = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    ngOnInit() {
        this.loadAll();
        this.principal.identity().then(account => {
            this.currentAccount = account;
        });
        this.registerChangeInStudents();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: Student) {
        return item.id;
    }

    saveStudent(groupId: number, student: Student) {
        const modalRef = this.modalService.open(StudentUpdateDialogComponent, { size: 'lg', backdrop: 'static' });
        modalRef.componentInstance.student = Object.assign({}, student);
        modalRef.componentInstance.groupId = groupId;
    }

    deleteStudent(groupId: number, student: Student) {
        const modalRef = this.modalService.open(StudentDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
        modalRef.componentInstance.student = Object.assign({}, student);
        modalRef.componentInstance.groupId = groupId;
    }

    registerChangeInStudents() {
        this.eventSubscriber = this.eventManager.subscribe('groupStudentListModification', response => this.loadAll());
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
