import {Component} from '@angular/core';
import {StorageService} from "../../services/storage.service";
import {Victorina} from "../../dto/victorina";
import {ActivatedRoute} from "@angular/router";
import {PersonRole} from "../../utils/person-role";
import {ApiService} from "../../services/api.service";
import {Person} from "../../dto/person";

@Component({
  selector: 'app-statistic',
  templateUrl: './statistic.component.html',
  styleUrl: './statistic.component.scss'
})
export class StatisticComponent {
  id:number
  victorina:Victorina
  panelOpenState = true

  winner:Person | null = null
  owner:Person | null = null
  isAdmin = this.storage.person?.role === PersonRole.ADMIN

  panelOpenStatePart= false

  constructor(protected storage:StorageService, private api:ApiService,activeRoute:ActivatedRoute) {
    this.id = activeRoute.snapshot.params['id']
    this.victorina = storage.victorinas[this.id]
    //this.victorina = storage.victorinas.filter( v => v.id === id ).shift() as Victorina
  }

  getOwner(){
    this.api.getPerson(this.victorina.ownerId).subscribe( response => {
      this.owner = response.body;
    })
  }

  getWinner(){
    this.api.getPerson(this.victorina.winnerId!).subscribe( response => {
      this.winner = response.body;
    })
  }



  persons = new Array<Person>()
  addPerson(id:string){
    this.api.getPerson(id).subscribe( response => {
      let person = response.body
      this.persons.push(person!)
    })
  }

  getPerson(id:string):Person | undefined  {
    return this.persons.find(p => p.id === id)
  }


}
