import { Component, OnInit } from '@angular/core';
import { TranslationItemService } from 'src/app/service/translation-item.service';

@Component({
  selector: 'app-page-header',
  templateUrl: './page-header.component.html',
  styleUrls: ['./page-header.component.sass']
})
export class PageHeaderComponent implements OnInit {
  constructor(private translationItemService: TranslationItemService) {}

  public searchQuery: string = '';

  ngOnInit() {}

  filter(query: string) {
    this.translationItemService.filterTranslationItems(query);
  }
}
