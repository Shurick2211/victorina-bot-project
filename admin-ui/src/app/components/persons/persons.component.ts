import {Component, OnInit} from '@angular/core';
import {StorageService} from "../../services/storage.service";
import {PersonRole} from "../../utils/person-role";
import {ApiService} from "../../services/api.service";
import {Victorina} from "../../dto/victorina";

@Component({
  selector: 'app-persons',
  templateUrl: './persons.component.html',
  styleUrl: './persons.component.scss'
})
export class PersonsComponent implements OnInit{

  roles = Object.values(PersonRole)

  curPage = 1
  iPerPage = 8
  isSend = false
  receivers: string[] = []



  addAll($event: Event){
    const target = $event.target as HTMLInputElement;
    const checkboxValue = target.checked;
    if (checkboxValue)
      this.receivers = this.storage.persons.map( person => person.id)
    else this.receivers = []
  }

  get isAll():boolean{
    return this.receivers.length === this.storage.persons.length
  }

  get arrPages():number{ return  Math.floor(this.storage.persons.length/this.iPerPage) }

  constructor(protected storage:StorageService, private api:ApiService) {
  }

  ngOnInit(): void {
      this.storage.getPersons(this.curPage - 1, this.iPerPage*2)
      if (this.storage.victorinas.length < 1) this.storage.refreshVictorins()
    }


  save(i: number) {
    this.api.savePerson(this.storage.person!.id, this.storage.persons[i]).subscribe(response=>{
      console.log(response.status)
    })
  }

  item(i:number): number {
    return i + this.iPerPage * (this.curPage - 1)
  }

  getFromDb(){
    this.storage.getPersons(this.curPage - 1, this.iPerPage)
  }

  getVictorina(id:String):Victorina | undefined {
    return  this.storage.victorinas.find(victorina => victorina.id === id)
  }

  getVictorinaIndex(id:String):number | undefined {
    const victorina = this.getVictorina(id)
    return  victorina ? this.storage.victorinas.indexOf(victorina) : undefined
  }

  addReceiver($event: Event, id: string) {
    const target = $event.target as HTMLInputElement;
    const checkboxValue = target.checked;
    if (checkboxValue) this.receivers.push(id)
    else this.receivers = this.receivers.filter( it => it !== id)
  }
}
