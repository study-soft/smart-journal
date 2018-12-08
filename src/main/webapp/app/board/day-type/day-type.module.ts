import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SharedModule } from 'app/shared';
import {
    DayTypeComponent,
    DayTypeDetailComponent,
    DayTypeUpdateComponent,
    DayTypeDeletePopupComponent,
    DayTypeDeleteDialogComponent,
    dayTypeRoute,
    dayTypePopupRoute
} from '.';

const ENTITY_STATES = [...dayTypeRoute, ...dayTypePopupRoute];

@NgModule({
    imports: [SharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        DayTypeComponent,
        DayTypeDetailComponent,
        DayTypeUpdateComponent,
        DayTypeDeleteDialogComponent,
        DayTypeDeletePopupComponent
    ],
    entryComponents: [DayTypeComponent, DayTypeUpdateComponent, DayTypeDeleteDialogComponent, DayTypeDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class DayTypeModule {}
