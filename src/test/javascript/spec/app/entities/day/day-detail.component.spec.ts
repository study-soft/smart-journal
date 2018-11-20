/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { SmartjournalTestModule } from '../../../test.module';
import { DayDetailComponent } from 'app/entities/day/day-detail.component';
import { Day } from 'app/shared/model/day.model';

describe('Component Tests', () => {
    describe('Day Management Detail Component', () => {
        let comp: DayDetailComponent;
        let fixture: ComponentFixture<DayDetailComponent>;
        const route = ({ data: of({ day: new Day(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [SmartjournalTestModule],
                declarations: [DayDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(DayDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(DayDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.day).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
