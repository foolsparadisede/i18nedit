import { Component, OnInit } from '@angular/core';
import { TranslationItem } from 'src/app/models/translation-item';
import { ApiService } from 'src/app/service/api.service';

@Component({
  selector: 'app-key-values-edit-page',
  templateUrl: './key-values-edit-page.component.html',
  styleUrls: ['./key-values-edit-page.component.sass']
})
export class KeyValuesEditPageComponent implements OnInit {
  public languages: string[] = ['de-DE', 'en-US'];

  public keys: TranslationItem[] = [];

  constructor(private api: ApiService) {}

  ngOnInit() {
    this.api.getKeys().then(res => (this.keys = res));
  }

  addKey() {
    const langs = [];
    this.languages.forEach(l => {
      langs.push({ language: l, string: '' });
    });
    const newKey = TranslationItem.fromJson({ key: '', translations: langs });
    newKey.setUpdated();
    this.keys.push(newKey);
  }

  submit() {
    const filtered = this.keys.filter(key => key.isUpdated());
    this.api.updateKeys(filtered);
  }
}
