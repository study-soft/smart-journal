import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { SmartjournalPartyModule } from './party/party.module';
import { SmartjournalSubjectModule } from './subject/subject.module';
import { SmartjournalStudentModule } from './student/student.module';
import { SmartjournalDayModule } from './day/day.module';
import { SmartjournalDayTypeModule } from './day-type/day-type.module';
import { SmartjournalBoardModule } from './board/board.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    // prettier-ignore
    imports: [
        SmartjournalPartyModule,
        SmartjournalSubjectModule,
        SmartjournalStudentModule,
        SmartjournalDayModule,
        SmartjournalDayTypeModule,
        SmartjournalBoardModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class SmartjournalEntityModule {}
