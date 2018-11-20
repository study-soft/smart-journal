/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { SmartjournalTestModule } from '../../../test.module';
import { DayTypeDetailComponent } from 'app/entities/day-type/day-type-detail.component';
import { DayType } from 'app/shared/model/day-type.model';

describe('Component Tests', () => {
    describe('DayType Management Detail Component', () => {
        let comp: DayTypeDetailComponent;
        let fixture: ComponentFixture<DayTypeDetailComponent>;
        const route = ({ data: of({ dayType: new DayType(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [SmartjournalTestModule],
                declarations: [DayTypeDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(DayTypeDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(DayTypeDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.dayType).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
