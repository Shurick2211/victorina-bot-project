import {Component, OnInit} from '@angular/core';
import {StorageService} from "../../services/storage.service";

@Component({
  selector: 'app-start',
  templateUrl: './start.component.html',
  styleUrls: ['./start.component.scss']
})
export class StartComponent implements OnInit{
  protected password:string =""

  constructor(public storage:StorageService) {
    storage.refreshPerson()
  }


  ngOnInit(): void {

  }

  save() {
    this.storage.person!.password = this.password
    this.storage.savePerson()
  }

  signIn() {
    if (this.storage.person!.password == this.password)
    this.storage.isAuth = true
  }
}
