import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { PageHeaderComponent } from './page-header.component';
import { TranslationItemService } from 'src/app/service/translation-item.service';
import { FormsModule } from '@angular/forms';

@NgModule({
  declarations: [PageHeaderComponent],
  imports: [CommonModule, FormsModule],
  providers: [TranslationItemService],
  exports: [PageHeaderComponent]
})
export class PageHeaderModule {}
