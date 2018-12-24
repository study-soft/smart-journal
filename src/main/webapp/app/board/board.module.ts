import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SharedModule } from 'app/shared';
import { boardPopupRoute, boardRoute } from 'app/board/board.route';
import { BoardComponent } from 'app/board/board.component';
import { BoardDetailComponent } from 'app/board/board-detail.component';
import { BoardUpdateComponent } from 'app/board/board-update.component';
import { BoardDeleteDialogComponent, BoardDeletePopupComponent } from 'app/board/board-delete-dialog.component';
import { DayModule } from 'app/board/day/day.module';
import { DayTypeModule } from 'app/board/day-type/day-type.module';
import { BoardChildComponent } from './board-child.component';
import { BoardJournalComponent } from './board-journal/board-journal.component';
import { BoardTasksComponent } from './board-tasks/board-tasks.component';

const ENTITY_STATES = [...boardRoute, ...boardPopupRoute];

@NgModule({
    imports: [SharedModule, DayModule, DayTypeModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        BoardComponent,
        BoardDetailComponent,
        BoardUpdateComponent,
        BoardDeleteDialogComponent,
        BoardDeletePopupComponent,
        BoardChildComponent,
        BoardJournalComponent,
        BoardTasksComponent
    ],
    entryComponents: [BoardComponent, BoardUpdateComponent, BoardDeleteDialogComponent, BoardDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class BoardModule {}
