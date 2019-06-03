import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TextareaAutosizeDirective } from './textarea-autosize.directive';
import { FormsModule } from '@angular/forms';

@NgModule({
  imports: [
    CommonModule,
    FormsModule,
  ],
  declarations: [TextareaAutosizeDirective],
  exports: [CommonModule, TextareaAutosizeDirective],
})
export class TextareaAutosizeModule { }
