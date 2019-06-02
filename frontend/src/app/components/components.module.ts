import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { KeyValueEditComponent } from './key-value-edit/key-value-edit.component';
import { PageHeaderModule } from './page-header/page-header.module';

@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    MatButtonModule,
    MatIconModule,
    PageHeaderModule
  ],
  declarations: [KeyValueEditComponent],
  exports: [KeyValueEditComponent, PageHeaderModule]
})
export class ComponentsModule {}
