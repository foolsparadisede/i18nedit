import { Component, OnInit } from '@angular/core';
import { TranslationItem } from 'src/app/models/translation-item';
import { ApiService } from 'src/app/service/api.service';
import { TranslationItemService } from 'src/app/service/translation-item.service';

@Component({
  selector: 'app-key-values-edit-page',
  templateUrl: './key-values-edit-page.component.html',
  styleUrls: ['./key-values-edit-page.component.sass']
})
export class KeyValuesEditPageComponent implements OnInit {
  public languages: string[] = ['de-DE', 'en-US'];
  public searchQuery = '';

  public keys: TranslationItem[] = [];

  constructor(
    private api: ApiService,
    private translationItemService: TranslationItemService
  ) {}

  ngOnInit() {
    this.api.getKeys().then(res => {
      this.keys = res;
      this.translationItemService.updateIndex(res);
    });
  }

  filter(query: string) {
    this.translationItemService.getFilteredTranslationItems(query);
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
    this.api.updateKeys(filtered).then(res => {
      this.keys = res;
      this.translationItemService.updateIndex(res);
    });
  }
}
