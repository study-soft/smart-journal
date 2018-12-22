import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { Group } from 'app/shared/model/group.model';
import { GroupService } from 'app/group/group.service';
import { GroupComponent } from 'app/group/group.component';
import { GroupDetailComponent } from 'app/group/group-detail.component';
import { GroupUpdateComponent } from 'app/group/group-update.component';
import { GroupDeletePopupComponent } from 'app/group/group-delete-dialog.component';
import { StudentUpdateDialogComponent } from 'app/group/student-update-dialog/student-update-dialog.component';

@Injectable({ providedIn: 'root' })
export class GroupResolve implements Resolve<Group> {
    constructor(private service: GroupService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<Group> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<Group>) => response.ok),
                map((group: HttpResponse<Group>) => group.body)
            );
        }
        return of(new Group());
    }
}

export const groupRoute: Routes = [
    {
        path: 'group',
        component: GroupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'smartjournalApp.group.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'group/:id/view',
        component: GroupDetailComponent,
        resolve: {
            group: GroupResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'smartjournalApp.group.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'group/new',
        component: GroupUpdateComponent,
        resolve: {
            group: GroupResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'smartjournalApp.group.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'group/:id/edit',
        component: GroupUpdateComponent,
        resolve: {
            group: GroupResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'smartjournalApp.group.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const groupPopupRoute: Routes = [
    {
        path: 'group/:id/delete',
        component: GroupDeletePopupComponent,
        resolve: {
            group: GroupResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'smartjournalApp.group.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'student/:id/edit',
        component: StudentUpdateDialogComponent,
    }
];
