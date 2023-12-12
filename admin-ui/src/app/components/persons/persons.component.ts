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
  constructor(protected storage:StorageService, protected api:ApiService) {
    storage.getPersons(0, 5)
  }


  protected readonly PersonRole = PersonRole;
}
