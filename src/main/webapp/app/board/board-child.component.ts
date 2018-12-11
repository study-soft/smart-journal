import { Component, Input, OnInit } from '@angular/core';
import { Board } from 'app/shared/model/board.model';

@Component({
    selector: 'jhi-board-child',
    templateUrl: './board-child.component.html',
    styles: []
})
export class BoardChildComponent implements OnInit {
    @Input() board: Board;

    constructor() {}

    ngOnInit() {}
}
