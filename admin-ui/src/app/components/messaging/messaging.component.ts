import {Component, EventEmitter, Input, Output} from '@angular/core';
import {ApiService} from "../../services/api.service";
import {StorageService} from "../../services/storage.service";

@Component({
  selector: 'app-messaging',
  templateUrl: './messaging.component.html',
  styleUrl: './messaging.component.scss'
})
export class MessagingComponent {

  @Input()
  receivers: string[] = []
  @Output()
  onMessaging = new EventEmitter<boolean>()

  message:string = ''
  isSend = false

  get isNotEmpty():boolean { return  this.message.length > 0 && this.receivers.length > 0}

  constructor(private apiService:ApiService, private storage:StorageService) {
  }
  sendMess(){
    if(this.isNotEmpty){
      this.receivers!.forEach( id =>{
        this.apiService.sendAdminMess(this.message!, this.storage.userId!, id)
      })
    }
    this.message = ''
  }

}
