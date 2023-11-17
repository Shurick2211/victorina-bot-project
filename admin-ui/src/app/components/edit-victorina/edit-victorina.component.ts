import { Component } from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {StorageService} from "../../services/storage.service";

@Component({
  selector: 'app-edit-victorina',
  templateUrl: './edit-victorina.component.html',
  styleUrls: ['./edit-victorina.component.css']
})
export class EditVictorinaComponent {

  id:number
  constructor(private activeRoute:ActivatedRoute, public storage:StorageService) {
    this.id = activeRoute.snapshot.params['id']
  }
}
