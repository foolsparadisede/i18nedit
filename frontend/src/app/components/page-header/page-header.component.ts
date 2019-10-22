import {Component, OnInit} from '@angular/core';
import {TranslationItemService} from 'src/app/service/translation-item.service';
import {ApiService} from '../../service/api.service';

@Component({
  selector: 'app-page-header',
  templateUrl: './page-header.component.html',
  styleUrls: ['./page-header.component.sass'],
})
export class PageHeaderComponent implements OnInit {
  constructor(
    private translationItemService: TranslationItemService,
    private apiService: ApiService,
  ) {
  }

  public searchQuery: string = '';

  ngOnInit() {
  }

  filter(query: string) {
    this.translationItemService.filterTranslationItems(query);
  }

  reimport() {
    this.translationItemService.setTranslations([]);
    this.apiService.reindex().then(() => {
      this.apiService.getKeys().then(res => {
        this.translationItemService.setTranslations(res);
      });
    });
  }
}
