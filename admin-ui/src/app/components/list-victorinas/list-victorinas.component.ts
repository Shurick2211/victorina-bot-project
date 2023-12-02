import {Component, OnInit} from '@angular/core';
import {StorageService} from "../../services/storage.service";


@Component({
  selector: 'app-list-victorinas',
  templateUrl: './list-victorinas.component.html',
  styleUrls: ['./list-victorinas.component.scss']
})
export class ListVictorinasComponent implements OnInit{

  panelOpenState = false

  iPerPage = 5;
  curPage = 1;

  constructor(public storage:StorageService) {

  }

  ngOnInit(): void {
    this.storage.refreshVictorins()
  }



}
