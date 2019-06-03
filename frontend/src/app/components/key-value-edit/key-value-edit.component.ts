import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';
import { TranslationItem } from 'src/app/models/translation-item';
import { Subject } from 'rxjs';

@Component({
  selector: 'app-key-value-edit',
  templateUrl: './key-value-edit.component.html',
  styleUrls: ['./key-value-edit.component.sass'],
})
export class KeyValueEditComponent implements OnInit {

  public subject: Subject<any> = new Subject();
  public observable = this.subject.asObservable();

  private _translationItem: TranslationItem;
  @Input()
  set translationItem(translationItem: TranslationItem) {
    this._translationItem = translationItem;
    setTimeout(() => {
      this.subject.next();
    });
  }

  get translationItem() {
    return this._translationItem;
  }

  constructor() {}

  ngOnInit() {}
}
