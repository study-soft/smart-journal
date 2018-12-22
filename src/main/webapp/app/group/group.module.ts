import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SharedModule } from 'app/shared';
import { groupPopupRoute, groupRoute } from 'app/group/group.route';
import { GroupComponent } from 'app/group/group.component';
import { GroupDetailComponent } from 'app/group/group-detail.component';
import { GroupUpdateComponent } from 'app/group/group-update.component';
import { GroupDeleteDialogComponent, GroupDeletePopupComponent } from 'app/group/group-delete-dialog.component';
import { StudentModule } from 'app/group/student/student.module';
import { GroupChildComponent } from './group-child.component';
import { GroupStudentsComponent } from './group-students/group-students.component';
import { GroupInformationComponent } from './group-information/group-information.component';
import { StudentUpdateDialogComponent } from './student-update-dialog/student-update-dialog.component';
import { StudentDeleteDialogComponent } from './student-delete-dialog/student-delete-dialog.component';

const ENTITY_STATES = [...groupRoute, ...groupPopupRoute];

@NgModule({
    imports: [SharedModule, StudentModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        GroupComponent,
        GroupDetailComponent,
        GroupUpdateComponent,
        GroupDeleteDialogComponent,
        GroupDeletePopupComponent,
        GroupChildComponent,
        GroupStudentsComponent,
        GroupInformationComponent,
        StudentUpdateDialogComponent,
        StudentDeleteDialogComponent
    ],
    entryComponents: [
        GroupComponent,
        GroupUpdateComponent,
        GroupDeleteDialogComponent,
        GroupDeletePopupComponent,
        StudentDeleteDialogComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class GroupModule {}
