import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { Board } from 'app/shared/model/board.model';
import { JhiAlertService, JhiEventManager } from 'ng-jhipster';
import { Subscription } from 'rxjs';
import { BoardService } from 'app/board/board.service';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Day } from 'app/shared/model/day.model';

@Component({
    selector: 'jhi-board-detail',
    templateUrl: './board-detail.component.html'
})
export class BoardDetailComponent implements OnInit, OnDestroy {
    board: Board;
    eventSubscriber: Subscription;

    constructor(private activatedRoute: ActivatedRoute,
                private eventManager: JhiEventManager,
                private boardService: BoardService,
                private jhiAlertService: JhiAlertService) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ board }) => {
            this.board = board;
        });

        this.registerChangeInDays();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    loadBoard() {
        this.boardService.find(this.board.id).subscribe(
            (res: HttpResponse<Board>) => {
                this.board = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message));
    }

    registerChangeInDays() {
        this.eventSubscriber = this.eventManager.subscribe('daysListModification',
                response => {
                    this.loadBoard();
                    console.log('on daysListModification: ', this.board);
                });
    }

    previousState() {
        window.history.back();
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
