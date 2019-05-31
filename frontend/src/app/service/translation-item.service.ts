import { Injectable } from '@angular/core';
import { TranslationItem } from '../models/translation-item';
import * as lunr from 'lunr';

@Injectable({
  providedIn: 'root'
})
export class TranslationItemService {
  private idx: any;

  constructor() {}

  updateIndex(translationItems: TranslationItem[]) {
    this.idx = lunr(function() {
      this.ref('id');
      this.field('key');

      translationItems.forEach(function(doc) {
        this.add(doc);
      }, this);
    });
  }

  getFilteredTranslationItems(
    query: string,
    languages?: string[]
  ): TranslationItem[] {
    console.log(query);

    let res: any = this.idx.search('*' + query + '*~1');
    console.log(res);
    return [];
  }
}
