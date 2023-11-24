import {Component, OnInit} from '@angular/core';
import {Person} from "../../dto/person";
import {StorageService} from "../../services/storage.service";

@Component({
  selector: 'app-start',
  templateUrl: './start.component.html',
  styleUrls: ['./start.component.css']
})
export class StartComponent implements OnInit{


  constructor(public storage:StorageService) {
    storage.refreshPerson()
  }


  ngOnInit(): void {

  }
}
