import { Component, Input, OnInit } from '@angular/core';
import { Student } from 'app/shared/model/student.model';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { StudentUpdateDialogComponent } from 'app/group/student-update-dialog/student-update-dialog.component';
import { StudentDeleteDialogComponent } from 'app/group/student-delete-dialog/student-delete-dialog.component';

@Component({
    selector: 'jhi-group-students',
    templateUrl: './group-students.component.html',
    styles: []
})
export class GroupStudentsComponent implements OnInit {
    @Input() groupId: number;
    @Input() students: Student[];

    constructor(private modalService: NgbModal) {}

    ngOnInit() {
        console.log('on init: groupId = ' + this.groupId);
    }

    trackId(index: number, item: Student): number {
        return item.id;
    }

    saveStudent(groupId: number, student: Student) {
        const modalRef = this.modalService.open(StudentUpdateDialogComponent, { size: 'lg', backdrop: 'static' });
        modalRef.componentInstance.student = student;
        console.log('in saveStudent: groupId = ' + groupId);
        modalRef.componentInstance.groupId = groupId;
    }

    deleteStudent(groupId: number, student: Student) {
        const modalRef = this.modalService.open(StudentDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
        modalRef.componentInstance.student = student;
        modalRef.componentInstance.groupId = groupId;
    }
}
