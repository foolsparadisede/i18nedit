import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { MatButtonModule } from '@angular/material/button';
import { KeyValueEditComponent } from './key-value-edit/key-value-edit.component';

@NgModule({
  imports: [CommonModule, FormsModule, MatButtonModule],
  declarations: [KeyValueEditComponent],
  exports: [KeyValueEditComponent]
})
export class ComponentsModule {}
