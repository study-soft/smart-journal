/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Observable, of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { SmartjournalTestModule } from '../../../test.module';
import { PartyComponent } from 'app/entities/party/party.component';
import { PartyService } from 'app/entities/party/party.service';
import { Party } from 'app/shared/model/party.model';

describe('Component Tests', () => {
    describe('Party Management Component', () => {
        let comp: PartyComponent;
        let fixture: ComponentFixture<PartyComponent>;
        let service: PartyService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [SmartjournalTestModule],
                declarations: [PartyComponent],
                providers: []
            })
                .overrideTemplate(PartyComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(PartyComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(PartyService);
        });

        it('Should call load all on init', () => {
            // GIVEN
            const headers = new HttpHeaders().append('link', 'link;link');
            spyOn(service, 'query').and.returnValue(
                of(
                    new HttpResponse({
                        body: [new Party(123)],
                        headers
                    })
                )
            );

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.query).toHaveBeenCalled();
            expect(comp.parties[0]).toEqual(jasmine.objectContaining({ id: 123 }));
        });
    });
});
