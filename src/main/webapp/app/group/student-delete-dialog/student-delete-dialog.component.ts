import { Component, OnInit } from '@angular/core';
import { Student } from 'app/shared/model/student.model';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';
import { GroupService } from 'app/group';

@Component({
  selector: 'jhi-student-delete-dialog',
  templateUrl: './student-delete-dialog.component.html',
  styles: []
})
export class StudentDeleteDialogComponent implements OnInit {
    student: Student;
    groupId: number;

    constructor(
        private groupService: GroupService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {}

    ngOnInit() {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(groupId: number, studentId: number) {
        this.groupService.deleteStudent(groupId, studentId).subscribe(response => {
            this.eventManager.broadcast({
                name: 'groupStudentListModification',
                content: 'Deleted an student from group'
            });
            this.activeModal.dismiss(true);
        });
    }
}
