/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { SmartjournalTestModule } from '../../../test.module';
import { BoardDetailComponent } from 'app/entities/board/board-detail.component';
import { Board } from 'app/shared/model/board.model';

describe('Component Tests', () => {
    describe('Board Management Detail Component', () => {
        let comp: BoardDetailComponent;
        let fixture: ComponentFixture<BoardDetailComponent>;
        const route = ({ data: of({ board: new Board(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [SmartjournalTestModule],
                declarations: [BoardDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(BoardDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(BoardDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.board).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
