import { Component, OnInit } from '@angular/core';
import { Student } from 'app/shared/model/student.model';
import { StudentService } from 'app/group/student';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiAlertService } from 'ng-jhipster';
import { Observable } from 'rxjs';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { GroupService } from 'app/group';

@Component({
    selector: 'jhi-student-update-dialog',
    templateUrl: './student-update-dialog.component.html',
    styles: []
})
export class StudentUpdateDialogComponent implements OnInit {
    student: Student;
    groupId: number;
    isSaving: boolean;

    constructor(
        private groupService: GroupService,
        private activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
    ) {}

    ngOnInit() {
        console.log('StudentUpdateDialogComponent on init: groupId = ' + this.groupId);
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmSave() {
        this.isSaving = true;
        if (this.student.id !== undefined) {
            this.subscribeToSaveResponse(this.groupService.updateStudent(this.groupId, this.student));
        } else {
            this.subscribeToSaveResponse(this.groupService.createStudent(this.groupId, this.student));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<Student>>) {
        result.subscribe((res: HttpResponse<Student>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.clear();
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
