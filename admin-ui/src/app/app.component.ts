import {Component, Inject, OnInit} from '@angular/core';
import {DOCUMENT} from "@angular/common";
import {Router} from "@angular/router";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})

export class AppComponent implements OnInit {
  title = 'admin-ui';
  isUser = false
  constructor(@Inject(DOCUMENT) private document: Document, private router:Router) {}

  ngOnInit(): void {
    // Accessing query parameters directly from the URL
    const queryParams = new URLSearchParams(this.document.location.search);
    const paramValue = queryParams.get('user');
    if(paramValue !==null ) this.isUser = paramValue.length > 0
    // Use paramValue as needed
    console.log('Query Param Value:', paramValue);
    // Perform actions with the received parameter
    this.router.navigateByUrl("/start/"+paramValue)
  }
}
