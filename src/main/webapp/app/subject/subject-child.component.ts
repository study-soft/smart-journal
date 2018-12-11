import { Component, Input, OnInit } from '@angular/core';
import { Subject } from 'app/shared/model/subject.model';

@Component({
    selector: 'jhi-subject-child',
    templateUrl: './subject-child.component.html',
    styles: []
})
export class SubjectChildComponent implements OnInit {
    @Input() subject: Subject;

    constructor() {}

    ngOnInit() {}
}
