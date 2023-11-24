import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppComponent } from './app.component';
import { RouterModule, RouterOutlet, Routes} from "@angular/router";
import { CreateVictorinaComponent } from './components/create-victorina/create-victorina.component';
import { ListVictorinasComponent } from './components/list-victorinas/list-victorinas.component';
import {FormsModule} from "@angular/forms";
import { QuestionComponent } from './components/question/question.component';
import {MatCardModule} from '@angular/material/card';
import {MatButtonModule} from "@angular/material/button";
import {MatCheckboxModule} from "@angular/material/checkbox";
import {ApiService} from "./services/api.service";
import {HttpClientModule} from "@angular/common/http";
import { StartComponent } from './components/start/start.component';




const appRoutes: Routes = [
  {path: 'start/:user', component: StartComponent},
  {path: 'create', component: CreateVictorinaComponent},
  {path: 'list', component: ListVictorinasComponent},
  {path: 'edit/:id', component: CreateVictorinaComponent},


]

@NgModule({
  declarations: [
    AppComponent,
    CreateVictorinaComponent,
    ListVictorinasComponent,
    QuestionComponent,
    StartComponent
  ],
  imports: [
    BrowserModule,
    RouterOutlet,
    FormsModule,
    RouterModule.forRoot(appRoutes),
    FormsModule,
    MatCardModule,
    MatButtonModule,
    MatCheckboxModule,
    HttpClientModule
  ],
  providers: [ApiService],
  bootstrap: [AppComponent]
})
export class AppModule { }
