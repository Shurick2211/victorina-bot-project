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
import {MatSortModule} from "@angular/material/sort";
import {MatIconModule} from "@angular/material/icon";
import {MatExpansionModule} from "@angular/material/expansion";
import {NgxPaginationModule} from "ngx-pagination";
import {StatisticComponent} from "./components/statistic/statistic.component";
import { CommonModule } from '@angular/common';
import {MatSidenavModule} from "@angular/material/sidenav";
import {PersonsComponent} from "./components/persons/persons.component";
import {ChannelsComponent} from "./components/channels/channels.component";
import {MessagingComponent} from "./components/messaging/messaging.component";
import {MatButtonToggleModule} from "@angular/material/button-toggle";



const appRoutes: Routes = [
  {path: 'start', component: StartComponent},
  {path: 'create', component: CreateVictorinaComponent},
  {path: 'list', component: ListVictorinasComponent},
  {path: 'persons', component: PersonsComponent},
  {path: 'channels', component: ChannelsComponent},
  {path: 'edit/:id', component: CreateVictorinaComponent},
  {path: 'statistic/:id', component: StatisticComponent},

]


@NgModule({
  declarations: [
    AppComponent,
    CreateVictorinaComponent,
    ListVictorinasComponent,
    QuestionComponent,
    StartComponent,
    StatisticComponent,
    PersonsComponent,
    ChannelsComponent,
    MessagingComponent,
  ],
    imports: [
        CommonModule,
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
        MatSortModule,
        MatIconModule,
        MatExpansionModule,
        NgxPaginationModule,
        MatSidenavModule,
        MatButtonToggleModule

    ],
  providers: [ApiService,
    provideAnimations(),
    provideHttpClient(),
    importProvidersFrom(MatNativeDateModule)
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
