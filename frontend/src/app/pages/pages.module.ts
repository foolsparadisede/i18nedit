import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { KeyValuesEditPageComponent } from './key-values-edit-page/key-values-edit-page.component';
import { ViewsModule } from '../views/views.module';
import { ComponentsModule } from '../components/components.module';
import { MatButtonModule } from '@angular/material/button';
import { ApiService } from '../service/api.service';
import { FormsModule } from '@angular/forms';
import { TranslationItemService } from '../service/translation-item.service';

@NgModule({
  imports: [
    CommonModule,
    ViewsModule,
    ComponentsModule,
    MatButtonModule,
    FormsModule
  ],
  providers: [ApiService, TranslationItemService],
  declarations: [KeyValuesEditPageComponent]
})
export class PagesModule {}
