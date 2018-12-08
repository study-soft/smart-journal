import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { Student } from 'app/shared/model/student.model';
import { StudentService } from './student.service';
import { StudentComponent } from './student.component';
import { StudentDetailComponent } from './student-detail.component';
import { StudentUpdateComponent } from './student-update.component';
import { StudentDeletePopupComponent } from './student-delete-dialog.component';

@Injectable({ providedIn: 'root' })
export class StudentResolve implements Resolve<Student> {
    constructor(private service: StudentService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<Student> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<Student>) => response.ok),
                map((student: HttpResponse<Student>) => student.body)
            );
        }
        return of(new Student());
    }
}

export const studentRoute: Routes = [
    {
        path: 'student',
        component: StudentComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'smartjournalApp.student.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'student/:id/view',
        component: StudentDetailComponent,
        resolve: {
            student: StudentResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'smartjournalApp.student.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'student/new',
        component: StudentUpdateComponent,
        resolve: {
            student: StudentResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'smartjournalApp.student.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'student/:id/edit',
        component: StudentUpdateComponent,
        resolve: {
            student: StudentResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'smartjournalApp.student.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const studentPopupRoute: Routes = [
    {
        path: 'student/:id/delete',
        component: StudentDeletePopupComponent,
        resolve: {
            student: StudentResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'smartjournalApp.student.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
