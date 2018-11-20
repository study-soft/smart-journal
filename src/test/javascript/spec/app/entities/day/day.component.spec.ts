/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Observable, of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { SmartjournalTestModule } from '../../../test.module';
import { DayComponent } from 'app/entities/day/day.component';
import { DayService } from 'app/entities/day/day.service';
import { Day } from 'app/shared/model/day.model';

describe('Component Tests', () => {
    describe('Day Management Component', () => {
        let comp: DayComponent;
        let fixture: ComponentFixture<DayComponent>;
        let service: DayService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [SmartjournalTestModule],
                declarations: [DayComponent],
                providers: []
            })
                .overrideTemplate(DayComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(DayComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(DayService);
        });

        it('Should call load all on init', () => {
            // GIVEN
            const headers = new HttpHeaders().append('link', 'link;link');
            spyOn(service, 'query').and.returnValue(
                of(
                    new HttpResponse({
                        body: [new Day(123)],
                        headers
                    })
                )
            );

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.query).toHaveBeenCalled();
            expect(comp.days[0]).toEqual(jasmine.objectContaining({ id: 123 }));
        });
    });
});
