import { TranslationString } from './translation-string';

export class TranslationItem {
  public id: string;
  public key: string;
  public translations: TranslationString[];

  public deleted?: boolean;
  private updated?: boolean;

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
  }

  public isUpdated(): boolean {
    return !!this.updated;
  }

  public delete() {
    this.deleted = true;
  }
}
