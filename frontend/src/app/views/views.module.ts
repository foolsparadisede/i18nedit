import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { DefaultViewComponent } from './default-view/default-view.component';

@NgModule({
  imports: [CommonModule],
  declarations: [DefaultViewComponent],
  exports: [DefaultViewComponent]
})
export class ViewsModule {}
