import { Component, OnInit } from '@angular/core';
import { TranslationItem } from 'src/app/models/translation-item';
import { ApiService } from 'src/app/service/api.service';
import { TranslationItemService } from 'src/app/service/translation-item.service';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-key-values-edit-page',
  templateUrl: './key-values-edit-page.component.html',
  styleUrls: ['./key-values-edit-page.component.sass']
})
export class KeyValuesEditPageComponent implements OnInit {
  public languages: string[] = ['de-DE', 'en-US'];
  public searchQuery = '';
  public translations: Observable<TranslationItem[]>;

  constructor(
    private api: ApiService,
    private translationItemService: TranslationItemService
  ) {}

  ngOnInit() {
    this.translations = this.translationItemService.filteredTranslations;
    this.api.getKeys().then(res => {
      this.translationItemService.setTranslations(res);
    });
  }

  filter(query: string) {
    this.translationItemService.filterTranslationItems(query);
  }

  addKey() {
    this.translationItemService.addKey();
  }

  submit() {
    const filtered = this.translationItemService.getUpdated();
    this.api.updateKeys(filtered).then(res => {
      this.translationItemService.setTranslations(res);
    });
  }
}
