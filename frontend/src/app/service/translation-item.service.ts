import { Injectable } from '@angular/core';
import { TranslationItem } from '../models/translation-item';
import * as lunr from 'lunr';
import { Subject, BehaviorSubject, Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class TranslationItemService {
  private idx: any;
  private translations: TranslationItem[];
  private languages: string[] = ['de-DE', 'en-US'];
  private filteredTranslationsSubject: Subject<
    TranslationItem[]
  > = new Subject();
  public filteredTranslations: Observable<
    TranslationItem[]
  > = this.filteredTranslationsSubject.asObservable();

  constructor() {}

  setTranslations(translationItems: TranslationItem[]) {
    this.translations = translationItems;
    const languages = this.languages;
    this.idx = lunr(function() {
      this.ref('id');
      this.field('key');
      languages.forEach(lang => {
        this.field(lang);
      });

      translationItems.forEach(function(doc: any) {
        doc = Object.assign({}, doc);
        doc.translations.forEach(translation => {
          doc[translation.language] = translation.string;
        });

        this.add(doc);
      }, this);
    });
  }

  filterTranslationItems(query: string, languages?: string[]) {
    let searchRes: any = this.idx.search(query + '*');
    let res: TranslationItem[] = [];

    searchRes.forEach(element => {
      res.push(this.translations.find(item => item.id === element.ref));
    });

    this.filteredTranslationsSubject.next(res);
  }

  getUpdated(): TranslationItem[] {
    return this.translations.filter(key => key.isUpdated());
  }
}
