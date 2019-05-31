import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { TranslationItem } from '../models/translation-item';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class ApiService {
  constructor(private http: HttpClient) {}

  getKeys(): Promise<TranslationItem[]> {
    return this.http
      .get(`${environment.apiUrl}/items`)
      .toPromise()
      .then((res: any[]) => {
        return res.map(item => TranslationItem.fromJson(item));
      });
  }

  updateKeys(changed: TranslationItem[]): Promise<TranslationItem[]> {
    return this.http
      .post(`${environment.apiUrl}/update`, changed.map(item => item.toJson()))
      .toPromise()
      .then((res: any[]) => {
        return res.map(item => TranslationItem.fromJson(item));
      });
  }
}
