/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { SmartjournalTestModule } from '../../../test.module';
import { SubjectUpdateComponent } from 'app/entities/subject/subject-update.component';
import { SubjectService } from 'app/entities/subject/subject.service';
import { Subject } from 'app/shared/model/subject.model';

describe('Component Tests', () => {
    describe('Subject Management Update Component', () => {
        let comp: SubjectUpdateComponent;
        let fixture: ComponentFixture<SubjectUpdateComponent>;
        let service: SubjectService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [SmartjournalTestModule],
                declarations: [SubjectUpdateComponent]
            })
                .overrideTemplate(SubjectUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(SubjectUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(SubjectService);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity', fakeAsync(() => {
                // GIVEN
                const entity = new Subject(123);
                spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.subject = entity;
                // WHEN
                comp.save();
                tick(); // simulate async

                // THEN
                expect(service.update).toHaveBeenCalledWith(entity);
                expect(comp.isSaving).toEqual(false);
            }));

            it('Should call create service on save for new entity', fakeAsync(() => {
                // GIVEN
                const entity = new Subject();
                spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.subject = entity;
                // WHEN
                comp.save();
                tick(); // simulate async

                // THEN
                expect(service.create).toHaveBeenCalledWith(entity);
                expect(comp.isSaving).toEqual(false);
            }));
        });
    });
});
