import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IBoard } from 'app/shared/model/board.model';
import { BoardService } from './board.service';

@Component({
    selector: 'jhi-board-delete-dialog',
    templateUrl: './board-delete-dialog.component.html'
})
export class BoardDeleteDialogComponent {
    board: IBoard;

    constructor(private boardService: BoardService, public activeModal: NgbActiveModal, private eventManager: JhiEventManager) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.boardService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'boardListModification',
                content: 'Deleted an board'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-board-delete-popup',
    template: ''
})
export class BoardDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ board }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(BoardDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
                this.ngbModalRef.componentInstance.board = board;
                this.ngbModalRef.result.then(
                    result => {
                        this.router.navigate([{ outlets: { popup: null } }], { replaceUrl: true, queryParamsHandling: 'merge' });
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router.navigate([{ outlets: { popup: null } }], { replaceUrl: true, queryParamsHandling: 'merge' });
                        this.ngbModalRef = null;
                    }
                );
            }, 0);
        });
    }

    ngOnDestroy() {
        this.ngbModalRef = null;
    }
}
