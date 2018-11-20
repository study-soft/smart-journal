import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { IBoard } from 'app/shared/model/board.model';
import { Principal } from 'app/core';
import { BoardService } from './board.service';

@Component({
    selector: 'jhi-board',
    templateUrl: './board.component.html'
})
export class BoardComponent implements OnInit, OnDestroy {
    boards: IBoard[];
    currentAccount: any;
    eventSubscriber: Subscription;

    constructor(
        private boardService: BoardService,
        private jhiAlertService: JhiAlertService,
        private eventManager: JhiEventManager,
        private principal: Principal
    ) {}

    loadAll() {
        this.boardService.query().subscribe(
            (res: HttpResponse<IBoard[]>) => {
                this.boards = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    ngOnInit() {
        this.loadAll();
        this.principal.identity().then(account => {
            this.currentAccount = account;
        });
        this.registerChangeInBoards();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: IBoard) {
        return item.id;
    }

    registerChangeInBoards() {
        this.eventSubscriber = this.eventManager.subscribe('boardListModification', response => this.loadAll());
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
