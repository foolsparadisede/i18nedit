import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { KeyValuesEditPageComponent } from './key-values-edit-page/key-values-edit-page.component';
import { ViewsModule } from '../views/views.module';
import { ComponentsModule } from '../components/components.module';
import { MatButtonModule } from '@angular/material/button';

@NgModule({
  imports: [CommonModule, ViewsModule, ComponentsModule, MatButtonModule],
  declarations: [KeyValuesEditPageComponent]
})
export class PagesModule {}
