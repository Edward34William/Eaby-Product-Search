import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { DetailComponent } from './search/detail/detail.component';
import { ListComponent } from './search/list/list.component';
import { WishDetailComponent } from './search/wish-detail/wish-detail.component';

const routes: Routes = [
  { path: '', redirectTo: 'list', pathMatch: 'full' },
  { path: 'list', component: ListComponent, data: { state: 'list'} },
  { path: 'pro-detail', component: DetailComponent, data: { state: 'pro-detail' } },
  { path: 'wish-detail', component: WishDetailComponent, data: { state: 'wish-detail' } },
  { path: '**', redirectTo: 'list' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes, { 
    useHash: true
    // scrollPositionRestoration: 'enabled'
  })],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
