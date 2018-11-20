import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IBoard } from 'app/shared/model/board.model';

@Component({
    selector: 'jhi-board-detail',
    templateUrl: './board-detail.component.html'
})
export class BoardDetailComponent implements OnInit {
    board: IBoard;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ board }) => {
            this.board = board;
        });
    }

    previousState() {
        window.history.back();
    }
}
