import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { Subject } from 'app/shared/model/subject.model';
import { SubjectService } from './subject.service';
import { SubjectComponent } from './subject.component';
import { SubjectDetailComponent } from './subject-detail.component';
import { SubjectUpdateComponent } from './subject-update.component';
import { SubjectDeletePopupComponent } from './subject-delete-dialog.component';
import { ISubject } from 'app/shared/model/subject.model';

@Injectable({ providedIn: 'root' })
export class SubjectResolve implements Resolve<ISubject> {
    constructor(private service: SubjectService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<Subject> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<Subject>) => response.ok),
                map((subject: HttpResponse<Subject>) => subject.body)
            );
        }
        return of(new Subject());
    }
}

export const subjectRoute: Routes = [
    {
        path: 'subject',
        component: SubjectComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'smartjournalApp.subject.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'subject/:id/view',
        component: SubjectDetailComponent,
        resolve: {
            subject: SubjectResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'smartjournalApp.subject.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'subject/new',
        component: SubjectUpdateComponent,
        resolve: {
            subject: SubjectResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'smartjournalApp.subject.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'subject/:id/edit',
        component: SubjectUpdateComponent,
        resolve: {
            subject: SubjectResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'smartjournalApp.subject.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const subjectPopupRoute: Routes = [
    {
        path: 'subject/:id/delete',
        component: SubjectDeletePopupComponent,
        resolve: {
            subject: SubjectResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'smartjournalApp.subject.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
