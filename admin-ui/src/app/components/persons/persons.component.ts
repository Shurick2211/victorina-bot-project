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
  constructor(protected storage:StorageService, private api:ApiService) {
    storage.getPersons(0, 20)
  }


  save(i: number) {
    this.api.savePerson(this.storage.person!.id, this.storage.persons[i]).subscribe(response=>{
      console.log(response.status)
    })
  }
}
