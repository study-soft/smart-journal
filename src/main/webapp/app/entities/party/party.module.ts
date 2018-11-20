import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SmartjournalSharedModule } from 'app/shared';
import {
    PartyComponent,
    PartyDetailComponent,
    PartyUpdateComponent,
    PartyDeletePopupComponent,
    PartyDeleteDialogComponent,
    partyRoute,
    partyPopupRoute
} from './';

const ENTITY_STATES = [...partyRoute, ...partyPopupRoute];

@NgModule({
    imports: [SmartjournalSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [PartyComponent, PartyDetailComponent, PartyUpdateComponent, PartyDeleteDialogComponent, PartyDeletePopupComponent],
    entryComponents: [PartyComponent, PartyUpdateComponent, PartyDeleteDialogComponent, PartyDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class SmartjournalPartyModule {}
