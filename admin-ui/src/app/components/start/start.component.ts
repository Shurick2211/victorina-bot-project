import {Component, OnInit} from '@angular/core';
import {StorageService} from "../../services/storage.service";

@Component({
  selector: 'app-start',
  templateUrl: './start.component.html',
  styleUrls: ['./start.component.scss']
})
export class StartComponent implements OnInit{
  protected password:string | null = null
  hide = true;

  constructor(public storage:StorageService) {
    storage.refreshPerson()
  }


  ngOnInit(): void {

  }

  save() {
    if(this.password !== null) {
      this.storage.person!.password = this.password
      this.storage.savePerson()
    }
  }

  signIn() {
    if (this.storage.person!.password == this.password)
    this.storage.isReg = true
  }
}
