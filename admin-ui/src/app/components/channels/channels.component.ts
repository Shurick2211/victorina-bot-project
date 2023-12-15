import {Component, OnInit} from '@angular/core';
import {StorageService} from "../../services/storage.service";


@Component({
  selector: 'app-channels',
  templateUrl: './channels.component.html',
  styleUrl: './channels.component.scss'
})
export class ChannelsComponent implements OnInit{
  iPerPage= 20
  curPage = 1

  constructor(protected storage:StorageService) {

  }

  item(i:number): number {
    return i + this.iPerPage * (this.curPage - 1)
  }

  ngOnInit(): void {
    this.storage.getChannels()
  }

}
