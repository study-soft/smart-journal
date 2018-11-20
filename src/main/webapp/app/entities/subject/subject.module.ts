import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SmartjournalSharedModule } from 'app/shared';
import {
    SubjectComponent,
    SubjectDetailComponent,
    SubjectUpdateComponent,
    SubjectDeletePopupComponent,
    SubjectDeleteDialogComponent,
    subjectRoute,
    subjectPopupRoute
} from './';

const ENTITY_STATES = [...subjectRoute, ...subjectPopupRoute];

@NgModule({
    imports: [SmartjournalSharedModule, RouterModule.forChild(ENTITY_STATES)],
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
export class SmartjournalSubjectModule {}
