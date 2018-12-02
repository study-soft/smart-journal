import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SmartjournalSharedModule } from 'app/shared';
import {
    GroupComponent,
    GroupDetailComponent,
    GroupUpdateComponent,
    GroupDeletePopupComponent,
    GroupDeleteDialogComponent,
    groupRoute,
    groupPopupRoute
} from './';

const ENTITY_STATES = [...groupRoute, ...groupPopupRoute];

@NgModule({
    imports: [SmartjournalSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [GroupComponent, GroupDetailComponent, GroupUpdateComponent, GroupDeleteDialogComponent, GroupDeletePopupComponent],
    entryComponents: [GroupComponent, GroupUpdateComponent, GroupDeleteDialogComponent, GroupDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class SmartjournalGroupModule {}
