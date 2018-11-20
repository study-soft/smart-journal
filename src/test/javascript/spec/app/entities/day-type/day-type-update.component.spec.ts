/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { SmartjournalTestModule } from '../../../test.module';
import { DayTypeUpdateComponent } from 'app/entities/day-type/day-type-update.component';
import { DayTypeService } from 'app/entities/day-type/day-type.service';
import { DayType } from 'app/shared/model/day-type.model';

describe('Component Tests', () => {
    describe('DayType Management Update Component', () => {
        let comp: DayTypeUpdateComponent;
        let fixture: ComponentFixture<DayTypeUpdateComponent>;
        let service: DayTypeService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [SmartjournalTestModule],
                declarations: [DayTypeUpdateComponent]
            })
                .overrideTemplate(DayTypeUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(DayTypeUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(DayTypeService);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity', fakeAsync(() => {
                // GIVEN
                const entity = new DayType(123);
                spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.dayType = entity;
                // WHEN
                comp.save();
                tick(); // simulate async

                // THEN
                expect(service.update).toHaveBeenCalledWith(entity);
                expect(comp.isSaving).toEqual(false);
            }));

            it('Should call create service on save for new entity', fakeAsync(() => {
                // GIVEN
                const entity = new DayType();
                spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.dayType = entity;
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
