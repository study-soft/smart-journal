import { Component, Input, OnInit } from '@angular/core';
import { Board } from 'app/shared/model/board.model';

@Component({
    selector: 'jhi-board-tasks',
    templateUrl: './board-tasks.component.html',
    styles: []
})
export class BoardTasksComponent implements OnInit {
    @Input() board: Board;

    constructor() {}

    ngOnInit() {}
}
