import {Component, OnInit} from '@angular/core';
import {StorageService} from "../../services/storage.service";
import {PersonRole} from "../../utils/person-role";


@Component({
  selector: 'app-list-victorinas',
  templateUrl: './list-victorinas.component.html',
  styleUrls: ['./list-victorinas.component.scss']
})
export class ListVictorinasComponent implements OnInit{

  panelOpenState = false

  iPerPage = 5;
  curPage = 1;

  isAdmin = this.storage.person?.role === PersonRole.ADMIN

  constructor(public storage:StorageService) {

  }

  ngOnInit(): void {
    this.storage.refreshVictorins()
  }


  pageChange($event: number) {
    console.log($event)
    this.curPage = $event
  }

  get pagedVictorinas() {
    const startIndex = (this.curPage - 1) * this.iPerPage;
    const endIndex = startIndex + this.iPerPage;
    return this.storage.victorinas.slice(startIndex, endIndex);
  }
}
