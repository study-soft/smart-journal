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
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { JournalDetailComponent } from './board-journal/journal-detail/journal-detail.component';
import { JournalUpdateComponent } from './board-journal/journal-update/journal-update.component';
import { DayTypeComponent } from 'app/board/day-type';
import { BoardInformationComponent } from './board-information/board-information.component';

const ENTITY_STATES = [...boardRoute, ...boardPopupRoute];

@NgModule({
    imports: [SharedModule, DayModule, DayTypeModule, FormsModule, ReactiveFormsModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        BoardComponent,
        BoardDetailComponent,
        BoardUpdateComponent,
        BoardDeleteDialogComponent,
        BoardDeletePopupComponent,
        BoardChildComponent,
        BoardJournalComponent,
        BoardTasksComponent,
        JournalDetailComponent,
        JournalUpdateComponent,
        DayTypeComponent,
        BoardInformationComponent,
        // TodoListComponent,
    ],
    entryComponents: [BoardComponent, BoardUpdateComponent, BoardDeleteDialogComponent, BoardDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class BoardModule {}
