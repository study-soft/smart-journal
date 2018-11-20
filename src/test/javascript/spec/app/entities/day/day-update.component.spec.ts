/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { SmartjournalTestModule } from '../../../test.module';
import { DayUpdateComponent } from 'app/entities/day/day-update.component';
import { DayService } from 'app/entities/day/day.service';
import { Day } from 'app/shared/model/day.model';

describe('Component Tests', () => {
    describe('Day Management Update Component', () => {
        let comp: DayUpdateComponent;
        let fixture: ComponentFixture<DayUpdateComponent>;
        let service: DayService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [SmartjournalTestModule],
                declarations: [DayUpdateComponent]
            })
                .overrideTemplate(DayUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(DayUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(DayService);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity', fakeAsync(() => {
                // GIVEN
                const entity = new Day(123);
                spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.day = entity;
                // WHEN
                comp.save();
                tick(); // simulate async

                // THEN
                expect(service.update).toHaveBeenCalledWith(entity);
                expect(comp.isSaving).toEqual(false);
            }));

            it('Should call create service on save for new entity', fakeAsync(() => {
                // GIVEN
                const entity = new Day();
                spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.day = entity;
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
