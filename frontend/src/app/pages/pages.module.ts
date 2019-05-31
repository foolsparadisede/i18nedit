import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { KeyValuesEditPageComponent } from './key-values-edit-page/key-values-edit-page.component';
import { ViewsModule } from '../views/views.module';
import { ComponentsModule } from '../components/components.module';
import { MatButtonModule } from '@angular/material/button';
import { ApiService } from '../service/api.service';

@NgModule({
  imports: [CommonModule, ViewsModule, ComponentsModule, MatButtonModule],
  providers: [ApiService],
  declarations: [KeyValuesEditPageComponent]
})
export class PagesModule {}
