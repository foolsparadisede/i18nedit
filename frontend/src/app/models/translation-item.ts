import { TranslationString } from './translation-string';
import { Subject, Observable } from 'rxjs';

export class TranslationItem {
  public id: string;
  public key: string;
  public translations: TranslationString[] = [];

  public deleted?: boolean;
  private updated?: boolean;

  public changeSubject: Subject<void> = new Subject();
  public changeObservable = this.changeSubject.asObservable();

  public static create(langs: string[]): TranslationItem {
    const res = new TranslationItem();
    res.translations = [];

    langs.forEach(l => {
      res.translations.push({ language: l, string: '' });
    });
    return res;
  }

  public static fromJson(json: any): TranslationItem {
    const res = new TranslationItem();
    if (json.id) {
      res.id = json.id;
    }
    if (json.key) {
      res.key = json.key;
    }
    if (json.translations) {
      res.translations = json.translations;
    }
    return res;
  }

  public toJson() {
    const res: any = {};
    res.id = this.id;
    res.key = this.key;
    res.translations = this.translations;
    if (this.deleted) {
      res.deleted = this.deleted;
    }
    return res;
  }

  public setUpdated() {
    this.updated = true;
    this.changeSubject.next();
  }

  public isUpdated(): boolean {
    return !!this.updated;
  }

  public delete() {
    this.setUpdated();
    this.deleted = true;
  }
}
