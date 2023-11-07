import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppComponent } from './app.component';
import {RouterModule, RouterOutlet, Routes} from "@angular/router";
import { CreateVictorinaComponent } from './components/create-victorina/create-victorina.component';
import { ListVictorinasComponent } from './components/list-victorinas/list-victorinas.component';



const appRoutes: Routes = [
  {path: '', component: CreateVictorinaComponent},
  {path: 'list', component: ListVictorinasComponent}

]

@NgModule({
  declarations: [
    AppComponent,
    CreateVictorinaComponent,
    ListVictorinasComponent
  ],
  imports: [
    BrowserModule,
    RouterOutlet,
    RouterModule.forRoot(appRoutes),
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
