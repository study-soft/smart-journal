/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { SmartjournalTestModule } from '../../../test.module';
import { BoardDeleteDialogComponent } from 'app/entities/board/board-delete-dialog.component';
import { BoardService } from 'app/entities/board/board.service';

describe('Component Tests', () => {
    describe('Board Management Delete Component', () => {
        let comp: BoardDeleteDialogComponent;
        let fixture: ComponentFixture<BoardDeleteDialogComponent>;
        let service: BoardService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [SmartjournalTestModule],
                declarations: [BoardDeleteDialogComponent]
            })
                .overrideTemplate(BoardDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(BoardDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(BoardService);
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
