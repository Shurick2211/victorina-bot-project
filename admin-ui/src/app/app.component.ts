import {Component, Inject, OnInit, signal} from '@angular/core';
import {DOCUMENT} from "@angular/common";
import {Router} from "@angular/router";
import {StorageService} from "./services/storage.service";
import {ApiService} from "./services/api.service";


@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})

export class AppComponent implements OnInit {
  title = 'admin-ui';
  textMess = '';


  constructor(@Inject(DOCUMENT) private document: Document, private router:Router,
              protected  storage:StorageService, private apiService:ApiService) {}

  ngOnInit(): void {
    // Accessing query parameters directly from the URL
    const queryParams = new URLSearchParams(this.document.location.search);
    const paramId = queryParams.get('user');
    let isUser = (paramId !==null) && (paramId.length > 0)
    // Use paramId as needed
    console.log('Query Param Value:', paramId);

    if (isUser) {
      this.storage.userId = paramId
    }



    // Perform actions with the received parameter
    this.router.navigateByUrl("/start")
  }

  sendMess() {
    if(this.textMess !== '')
    this.apiService.sendAdminMess(this.textMess, this.storage.person!.id)
    this.textMess =''
  }


}
