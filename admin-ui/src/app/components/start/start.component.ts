import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from "@angular/router";

@Component({
  selector: 'app-start',
  templateUrl: './start.component.html',
  styleUrls: ['./start.component.css']
})
export class StartComponent implements OnInit{
  userId:any


  constructor(private activeRoute:ActivatedRoute) {

  }


  ngOnInit(): void {
    this.activeRoute.params.subscribe(param => {
      this.userId = param['user']
    })

  }
}
