import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'jhi-todo-list',
  templateUrl: './todo-list.component.html',
  styles: []
})
export class TodoListComponent implements OnInit {

    items: string[];

  constructor() { }

  ngOnInit() {
      this.items = [];
  }

    addItem(inputText: string) {
        if (inputText) {
            this.items.push(inputText);
        }
    }

    removeItem(i: number) {
        this.items.splice(i, 1);
    }

}
