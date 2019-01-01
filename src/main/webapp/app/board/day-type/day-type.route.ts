import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { DayType } from 'app/shared/model/day-type.model';
import { DayTypeService } from './day-type.service';
import { DayTypeComponent } from './day-type.component';
import { DayTypeDetailComponent } from './day-type-detail.component';
import { DayTypeUpdateComponent } from './day-type-update.component';
import { DayTypeDeletePopupComponent } from './day-type-delete-dialog.component';

@Injectable({ providedIn: 'root' })
export class DayTypeResolve implements Resolve<DayType> {
    constructor(private service: DayTypeService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<DayType> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<DayType>) => response.ok),
                map((dayType: HttpResponse<DayType>) => dayType.body)
            );
        }
        return of(new DayType());
    }
}

export const dayTypeRoute: Routes = [
    {
        path: 'board/:id/task',
        component: DayTypeComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'smartjournalApp.dayType.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'board/:boardId/task/:id/view',
        component: DayTypeDetailComponent,
        resolve: {
            dayType: DayTypeResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'smartjournalApp.dayType.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'board/:id/task/new',
        component: DayTypeUpdateComponent,
        resolve: {
            dayType: DayTypeResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'smartjournalApp.dayType.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'board/:boardId/task/:id/edit',
        component: DayTypeUpdateComponent,
        resolve: {
            dayType: DayTypeResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'smartjournalApp.dayType.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const dayTypePopupRoute: Routes = [
    {
        path: 'board/:boardId/task/:id/delete',
        component: DayTypeDeletePopupComponent,
        resolve: {
            dayType: DayTypeResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'smartjournalApp.dayType.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
