import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {ApiService} from "../../services/api.service";
import {Person} from "../../dto/person";

@Component({
  selector: 'app-start',
  templateUrl: './start.component.html',
  styleUrls: ['./start.component.css']
})
export class StartComponent implements OnInit{
  userId:any = null

  person: Person | null = null
  constructor(private api:ApiService, private activeRoute:ActivatedRoute) {

  }


  ngOnInit(): void {

    this.activeRoute.params.subscribe(param => {
      this.userId = param['user']
    })

    this.api.getPerson(this.userId).subscribe(response => {
        this.person = response.body
        console.log(this.person?.userName)
    })


  }
}
