import {Component, OnInit} from '@angular/core';
import {StorageService} from "../../services/storage.service";

@Component({
  selector: 'app-list-victorinas',
  templateUrl: './list-victorinas.component.html',
  styleUrls: ['./list-victorinas.component.css']
})
export class ListVictorinasComponent implements OnInit{


  constructor(public storage:StorageService) {
  }

  ngOnInit(): void {
    this.storage.refreshVictorins()
  }



}
