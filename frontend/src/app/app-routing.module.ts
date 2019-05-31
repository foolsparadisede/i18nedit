import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { KeyValuesEditPageComponent } from './pages/key-values-edit-page/key-values-edit-page.component';

const routes: Routes = [
  {
    path: 'keyValuesEdit',
    component: KeyValuesEditPageComponent
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {}
