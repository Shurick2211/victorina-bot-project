import { Component } from '@angular/core';
import {StorageService} from "../../services/storage.service";
import {PersonRole} from "../../utils/person-role";
import {ApiService} from "../../services/api.service";

@Component({
  selector: 'app-persons',
  templateUrl: './persons.component.html',
  styleUrl: './persons.component.scss'
})
export class PersonsComponent {

  roles = Object.values(PersonRole)

  curPage = 1
  iPerPage = 8

  constructor(protected storage:StorageService, private api:ApiService) {
    storage.getPersons(this.curPage - 1, this.iPerPage*2)
  }


  save(i: number) {
    this.api.savePerson(this.storage.person!.id, this.storage.persons[i]).subscribe(response=>{
      console.log(response.status)
    })
  }

  item(i:number): number {
    const rez = i + this.iPerPage * (this.curPage - 1)
    //if(rez >= this.storage.persons.length - 1) this.getFromDb()
    return rez
  }

  getFromDb(){
    this.storage.getPersons(this.curPage - 1, this.iPerPage)
  }
}
