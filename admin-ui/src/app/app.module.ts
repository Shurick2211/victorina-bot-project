import {importProvidersFrom, NgModule} from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppComponent } from './app.component';
import { RouterModule, RouterOutlet, Routes} from "@angular/router";
import { CreateVictorinaComponent } from './components/create-victorina/create-victorina.component';
import { ListVictorinasComponent } from './components/list-victorinas/list-victorinas.component';
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import { QuestionComponent } from './components/question/question.component';
import {MatCardModule} from '@angular/material/card';
import {MatButtonModule} from "@angular/material/button";
import {MatCheckboxModule} from "@angular/material/checkbox";
import {ApiService} from "./services/api.service";
import {HttpClientModule, provideHttpClient} from "@angular/common/http";
import { StartComponent } from './components/start/start.component';
import { MatDatepickerModule} from "@angular/material/datepicker";
import {MatInputModule} from "@angular/material/input";
import {MatNativeDateModule} from "@angular/material/core";
import {MatFormFieldModule} from "@angular/material/form-field";
import {provideAnimations, BrowserAnimationsModule} from "@angular/platform-browser/animations";
import {MatSlideToggleModule} from "@angular/material/slide-toggle";
import {MatSelectModule} from "@angular/material/select";
import {MatTableModule} from "@angular/material/table";
import {MatSortModule} from "@angular/material/sort";




const appRoutes: Routes = [
  {path: 'start', component: StartComponent},
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
    HttpClientModule,
    MatDatepickerModule,
    MatInputModule,
    ReactiveFormsModule,
    MatNativeDateModule,
    MatFormFieldModule,
    BrowserAnimationsModule,
    MatSlideToggleModule,
    MatSelectModule,
    MatTableModule,
    MatSortModule

  ],
  providers: [ApiService,
    provideAnimations(),
    provideHttpClient(),
    importProvidersFrom(MatNativeDateModule)
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
