/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { SmartjournalTestModule } from '../../../test.module';
import { SubjectDeleteDialogComponent } from 'app/entities/subject/subject-delete-dialog.component';
import { SubjectService } from 'app/entities/subject/subject.service';

describe('Component Tests', () => {
    describe('Subject Management Delete Component', () => {
        let comp: SubjectDeleteDialogComponent;
        let fixture: ComponentFixture<SubjectDeleteDialogComponent>;
        let service: SubjectService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [SmartjournalTestModule],
                declarations: [SubjectDeleteDialogComponent]
            })
                .overrideTemplate(SubjectDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(SubjectDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(SubjectService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('confirmDelete', () => {
            it('Should call delete service on confirmDelete', inject(
                [],
                fakeAsync(() => {
                    // GIVEN
                    spyOn(service, 'delete').and.returnValue(of({}));

                    // WHEN
                    comp.confirmDelete(123);
                    tick();

                    // THEN
                    expect(service.delete).toHaveBeenCalledWith(123);
                    expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
                })
            ));
        });
    });
});
