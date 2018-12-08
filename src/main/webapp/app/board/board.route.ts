import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { Board } from 'app/shared/model/board.model';
import { BoardDetailComponent } from './board-detail.component';
import { BoardUpdateComponent } from './board-update.component';
import { BoardDeletePopupComponent } from './board-delete-dialog.component';
import { BoardComponent } from 'app/board/board.component';
import { BoardService } from 'app/board/board.service';

@Injectable({ providedIn: 'root' })
export class BoardResolve implements Resolve<Board> {
    constructor(private service: BoardService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<Board> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<Board>) => response.ok),
                map((board: HttpResponse<Board>) => board.body)
            );
        }
        return of(new Board());
    }
}

export const boardRoute: Routes = [
    {
        path: 'board',
        component: BoardComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'smartjournalApp.board.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'board/:id/view',
        component: BoardDetailComponent,
        resolve: {
            board: BoardResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'smartjournalApp.board.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'board/new',
        component: BoardUpdateComponent,
        resolve: {
            board: BoardResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'smartjournalApp.board.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'board/:id/edit',
        component: BoardUpdateComponent,
        resolve: {
            board: BoardResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'smartjournalApp.board.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const boardPopupRoute: Routes = [
    {
        path: 'board/:id/delete',
        component: BoardDeletePopupComponent,
        resolve: {
            board: BoardResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'smartjournalApp.board.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
