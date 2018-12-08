import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SharedModule } from 'app/shared';
import {
    DayComponent,
    DayDetailComponent,
    DayUpdateComponent,
    DayDeletePopupComponent,
    DayDeleteDialogComponent,
    dayRoute,
    dayPopupRoute
} from '.';

const ENTITY_STATES = [...dayRoute, ...dayPopupRoute];

@NgModule({
    imports: [SharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [DayComponent, DayDetailComponent, DayUpdateComponent, DayDeleteDialogComponent, DayDeletePopupComponent],
    entryComponents: [DayComponent, DayUpdateComponent, DayDeleteDialogComponent, DayDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class DayModule {}
