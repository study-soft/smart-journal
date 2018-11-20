import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SmartjournalSharedModule } from 'app/shared';
import {
    DayComponent,
    DayDetailComponent,
    DayUpdateComponent,
    DayDeletePopupComponent,
    DayDeleteDialogComponent,
    dayRoute,
    dayPopupRoute
} from './';

const ENTITY_STATES = [...dayRoute, ...dayPopupRoute];

@NgModule({
    imports: [SmartjournalSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [DayComponent, DayDetailComponent, DayUpdateComponent, DayDeleteDialogComponent, DayDeletePopupComponent],
    entryComponents: [DayComponent, DayUpdateComponent, DayDeleteDialogComponent, DayDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class SmartjournalDayModule {}
