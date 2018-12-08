import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { subjectPopupRoute, subjectRoute } from 'app/subject/subject.route';
import { SubjectComponent } from 'app/subject/subject.component';
import { SubjectDetailComponent } from 'app/subject/subject-detail.component';
import { SubjectUpdateComponent } from 'app/subject/subject-update.component';
import { SubjectDeleteDialogComponent, SubjectDeletePopupComponent } from 'app/subject/subject-delete-dialog.component';
import { SharedModule } from 'app/shared';

const ENTITY_STATES = [...subjectRoute, ...subjectPopupRoute];

@NgModule({
    imports: [SharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        SubjectComponent,
        SubjectDetailComponent,
        SubjectUpdateComponent,
        SubjectDeleteDialogComponent,
        SubjectDeletePopupComponent
    ],
    entryComponents: [SubjectComponent, SubjectUpdateComponent, SubjectDeleteDialogComponent, SubjectDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class SubjectModule {}
