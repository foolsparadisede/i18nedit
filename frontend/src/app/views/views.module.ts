import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ComponentsModule } from '../components/components.module';
import { DefaultViewComponent } from './default-view/default-view.component';

@NgModule({
  imports: [CommonModule, ComponentsModule],
  declarations: [DefaultViewComponent],
  exports: [DefaultViewComponent]
})
export class ViewsModule {}
