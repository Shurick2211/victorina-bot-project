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

  isSend = false
  receivers: string[] = []


  constructor(protected storage:StorageService) {

  }

  item(i:number): number {
    return i + this.iPerPage * (this.curPage - 1)
  }

  ngOnInit(): void {
    this.storage.getChannels()
  }


  addReceiver($event: Event, channelId: string) {
    const target = $event.target as HTMLInputElement;
    const checkboxValue = target.checked;
    if (checkboxValue) this.receivers.push(channelId)
    else this.receivers = this.receivers.filter( id => id !== channelId)
  }


  addAll($event: Event){
    const target = $event.target as HTMLInputElement;
    const checkboxValue = target.checked;
    if (checkboxValue)
      this.receivers = this.storage.channels.map( chn => chn.channelId)
    else this.receivers = []
    console.log(this.receivers)

  }

  get isAll():boolean{
    return this.receivers.length === this.storage.channels.length
  }
}
