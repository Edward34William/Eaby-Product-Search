import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { ReactiveFormsModule, FormsModule } from '@angular/forms';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MatTooltipModule, MatButtonModule, MatCheckboxModule, MatFormFieldModule, MatInputModule, MatAutocompleteModule } from '@angular/material';
import { HttpClientModule } from '@angular/common/http';
import { RoundProgressModule } from 'angular-svg-round-progressbar';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { SearchComponent } from './search/search.component';
import { PresentComponent } from './search/present.component';
import { ListComponent } from './search/list/list.component';
import { DetailComponent } from './search/detail/detail.component';
import { WishComponent } from './search/list/wish/wish.component';
import { ResultComponent } from './search/list/result/result.component';
import { WishDetailComponent } from './search/wish-detail/wish-detail.component';

@NgModule({
  declarations: [
    AppComponent,
    SearchComponent,
    PresentComponent,
    ListComponent,
    DetailComponent,
    WishComponent,
    ResultComponent,
    WishDetailComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    ReactiveFormsModule,
    FormsModule,
    BrowserAnimationsModule,
    MatButtonModule,
    MatCheckboxModule,
    MatFormFieldModule,
    MatInputModule,
    MatAutocompleteModule,
    MatTooltipModule,
    HttpClientModule,
    RoundProgressModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
