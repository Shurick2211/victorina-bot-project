import {Component, Inject, OnInit} from '@angular/core';
import {DOCUMENT} from "@angular/common";
import {Router} from "@angular/router";
import {StorageService} from "./services/storage.service";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})

export class AppComponent implements OnInit {
  title = 'admin-ui';
  isUser = false
  constructor(@Inject(DOCUMENT) private document: Document, private router:Router, private  storage:StorageService) {}

  ngOnInit(): void {
    // Accessing query parameters directly from the URL
    const queryParams = new URLSearchParams(this.document.location.search);
    const paramId = queryParams.get('user');
    this.isUser = (paramId !==null) && (paramId.length > 0)
    // Use paramId as needed
    console.log('Query Param Value:', paramId);

    if (this.isUser) {
      this.storage.userId = paramId
    }

    // Perform actions with the received parameter
    this.router.navigateByUrl("/start")
  }
}
