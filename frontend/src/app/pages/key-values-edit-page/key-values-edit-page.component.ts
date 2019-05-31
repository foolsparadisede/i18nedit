import { Component, OnInit } from '@angular/core';
import { TranslationItem } from 'src/app/models/translation-item';

@Component({
  selector: 'app-key-values-edit-page',
  templateUrl: './key-values-edit-page.component.html',
  styleUrls: ['./key-values-edit-page.component.sass']
})
export class KeyValuesEditPageComponent implements OnInit {
  public languages: string[] = ['de-DE', 'en-US'];

  public keys: TranslationItem[] = [
    {
      key: 'hello.label',
      translations: [
        { language: 'de-DE', string: 'Hallo' },
        { language: 'en-US', string: 'Hello' }
      ]
    },

    {
      key: 'hello2.label',
      translations: [
        { language: 'de-DE', string: 'Hallo2' },
        { language: 'en-US', string: 'Hello2' }
      ]
    }
  ];

  constructor() {}

  ngOnInit() {}

  addKey() {
    const langs = [];
    this.languages.forEach(l => {
      langs.push({ language: l, string: '' });
    });
    this.keys.push({ key: '', translations: langs });
  }

  submit() {
    console.log(this.keys);
  }
}
