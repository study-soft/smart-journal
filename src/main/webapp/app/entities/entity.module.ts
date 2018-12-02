import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { SmartjournalSubjectModule } from './subject/subject.module';
import { SmartjournalStudentModule } from './student/student.module';
import { SmartjournalDayModule } from './day/day.module';
import { SmartjournalDayTypeModule } from './day-type/day-type.module';
import { SmartjournalBoardModule } from './board/board.module';
import { SmartjournalGroupModule } from 'app/entities/group/Group.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    // prettier-ignore
    imports: [
        SmartjournalGroupModule,
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
