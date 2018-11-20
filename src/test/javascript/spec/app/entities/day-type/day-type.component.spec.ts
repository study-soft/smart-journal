/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Observable, of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { SmartjournalTestModule } from '../../../test.module';
import { DayTypeComponent } from 'app/entities/day-type/day-type.component';
import { DayTypeService } from 'app/entities/day-type/day-type.service';
import { DayType } from 'app/shared/model/day-type.model';

describe('Component Tests', () => {
    describe('DayType Management Component', () => {
        let comp: DayTypeComponent;
        let fixture: ComponentFixture<DayTypeComponent>;
        let service: DayTypeService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [SmartjournalTestModule],
                declarations: [DayTypeComponent],
                providers: []
            })
                .overrideTemplate(DayTypeComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(DayTypeComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(DayTypeService);
        });

        it('Should call load all on init', () => {
            // GIVEN
            const headers = new HttpHeaders().append('link', 'link;link');
            spyOn(service, 'query').and.returnValue(
                of(
                    new HttpResponse({
                        body: [new DayType(123)],
                        headers
                    })
                )
            );

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.query).toHaveBeenCalled();
            expect(comp.dayTypes[0]).toEqual(jasmine.objectContaining({ id: 123 }));
        });
    });
});
