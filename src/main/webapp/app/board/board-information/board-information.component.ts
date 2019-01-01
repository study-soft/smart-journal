import { Component, Input, OnInit } from '@angular/core';
import { Board } from 'app/shared/model/board.model';

@Component({
    selector: 'jhi-board-information',
    templateUrl: './board-information.component.html',
    styles: []
})
export class BoardInformationComponent implements OnInit {
    @Input() board: Board;

    constructor() {}

    ngOnInit() {}
}
