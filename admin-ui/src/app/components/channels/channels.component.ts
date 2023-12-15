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
    const target = $event.target as HTMLInputElement; // Casting event target as HTMLInputElement
    if (target && target.type === 'checkbox') {
      const checkboxValue = target.checked; // Retrieve the checkbox value
      console.log('Checkbox value:', checkboxValue);
      // Now you can use the checkboxValue as needed within this method
    }
  }
}
