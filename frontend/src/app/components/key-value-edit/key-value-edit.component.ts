import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';
import { TranslationItem } from 'src/app/models/translation-item';

@Component({
  selector: 'app-key-value-edit',
  templateUrl: './key-value-edit.component.html',
  styleUrls: ['./key-value-edit.component.sass']
})
export class KeyValueEditComponent implements OnInit {
  @Input() translationItem: TranslationItem;

  constructor() {}

  ngOnInit() {}

  autogrow(ev) {
    let textArea = ev.target;
    setTimeout(() => {
      textArea.style.overflow = 'hidden';
      textArea.style.height = '0px';
      textArea.style.height = textArea.scrollHeight + 'px';
    });
  }
}
