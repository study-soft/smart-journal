import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { Day } from 'app/shared/model/day.model';
import { DayService } from './day.service';
import { DayComponent } from './day.component';
import { DayDetailComponent } from './day-detail.component';
import { DayUpdateComponent } from './day-update.component';
import { DayDeletePopupComponent } from './day-delete-dialog.component';

@Injectable({ providedIn: 'root' })
export class DayResolve implements Resolve<Day> {
    constructor(private service: DayService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<Day> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<Day>) => response.ok),
                map((day: HttpResponse<Day>) => day.body)
            );
        }
        return of(new Day());
    }
}

export const dayRoute: Routes = [
    {
        path: 'day',
        component: DayComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'smartjournalApp.day.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'day/:id/view',
        component: DayDetailComponent,
        resolve: {
            day: DayResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'smartjournalApp.day.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'day/new',
        component: DayUpdateComponent,
        resolve: {
            day: DayResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'smartjournalApp.day.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'day/:id/edit',
        component: DayUpdateComponent,
        resolve: {
            day: DayResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'smartjournalApp.day.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const dayPopupRoute: Routes = [
    {
        path: 'day/:id/delete',
        component: DayDeletePopupComponent,
        resolve: {
            day: DayResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'smartjournalApp.day.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
