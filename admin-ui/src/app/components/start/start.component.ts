import {Component, OnInit} from '@angular/core';
import {StorageService} from "../../services/storage.service";

@Component({
  selector: 'app-start',
  templateUrl: './start.component.html',
  styleUrls: ['./start.component.scss']
})
export class StartComponent implements OnInit{
  protected password:string | null = null
  hide = true;

  error:string|null = null
  rules = "If you created quiz for channel with prize, in day of start in 8:00 GMT, " +
    "in your channel the bot send message with name of your quiz and button for start the quiz. " +
    "The bot will only allow users subscribed to the channel to participate in the quiz. " +
    "If user is`n subscribed - the bot send channel`s link for subscribe, and after it allow " +
    "to participate in the quiz!\n" +
    "The day after end date in 10:00 GMT the bot will choose a winner randomly among those who right answered!" +
    "And all of them will received message with winner`s name! After it the winner will be asked for the delivery " +
    "address, and the bot will forward information about the winner and his address to the owner of the quiz."

  constructor(public storage:StorageService) {
    storage.refreshPerson()
  }


  ngOnInit(): void {

  }

  save() {
    if(this.password !== null) {
      this.storage.person!.password = this.password
      this.storage.savePerson()
    } else this.error = "Password is empty!"
  }

  signIn() {
    if (this.storage.person!.password == this.password)
      this.storage.isReg = true
    else this.error = "Password is wrong!"
  }

  pressEnter() {
    if (this.storage.person!.password !== null) this.signIn()
    else this.save()
  }
}
