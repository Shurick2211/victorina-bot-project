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
  findText:string|null = null

  constructor(public storage:StorageService) {

  }

  ngOnInit(): void {
    this.storage.refreshVictorins()
  }

   item(i:number): number {
    return i + this.iPerPage * (this.curPage - 1)
  }

  find() {

  }
}
